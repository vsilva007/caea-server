package ad.smartstudio.caea.controller;

import ad.smartstudio.caea.aspect.Audit;
import ad.smartstudio.caea.model.dto.CaeaTableLine;
import ad.smartstudio.caea.model.entity.ContactEmail;
import ad.smartstudio.caea.model.entity.ContactSms;
import ad.smartstudio.caea.model.entity.EmailNotification;
import ad.smartstudio.caea.model.entity.Usuari;
import ad.smartstudio.caea.service.NotificationService;
import ad.smartstudio.caea.service.UserService;
import ad.smartstudio.caea.util.EncryptionUtils;
import com.sendgrid.*;
import error.ApiException;
import error.RestExceptionHandler;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static ad.smartstudio.caea.security.utils.Constants.*;
import static ad.smartstudio.caea.util.CommonUtils.parseId;

@RestController
@RequestMapping(path = "/v1")
@Tag(name = "usuari", description = "API d'usuaris")
public class UserController extends RestExceptionHandler {
	private final static Log LOG = LogFactory.getLog(UserController.class);
	UserService service;
	NotificationService notificationService;
	SendGrid sendGrid;
	@Value("${server.url}")
	private String url;

	@Autowired
	public UserController(UserService service, SendGrid sendGrid, NotificationService notificationService) {
		this.service = service;
		this.sendGrid = sendGrid;
		this.notificationService = notificationService;
	}

	@Audit(message = "authenticate")
	@Operation(summary = "Login", description = "Autenticació", tags = { "usuari" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Usuari.class)))) })
	@PostMapping(value = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Usuari> login(@RequestBody Usuari usuari) throws ApiException {
		Usuari actualUsuari = service.login(usuari);
		if (null != actualUsuari) {
			if (!actualUsuari.getActive()) {
				throw new ApiException(HttpStatus.UNAUTHORIZED, "Compte inactiu. Revisa el teu correu.");
			}
			actualUsuari.setLastLogin(new Date().getTime());
			actualUsuari.setLoginCount(actualUsuari.getLoginCount() + 1);
			service.save(actualUsuari);
			actualUsuari.setPassword(null);
			actualUsuari.setToken(addAuthentication(actualUsuari));
			return ResponseEntity.ok(actualUsuari);
		}
		throw new ApiException(HttpStatus.UNAUTHORIZED, "Credencials incorrectes, torna a intentar si us plau");
	}

	public String addAuthentication(Usuari usuari) {
		String token;
		LOG.info("[TokenAuthenticationService] - login for user: " + usuari.getLogin());
		Map<String, Object> map = new HashMap<>();
		map.put("role", usuari.getRoleId());
		token = Jwts.builder().setClaims(map).setSubject(usuari.getId().toString()).setIssuedAt(new Date()).setIssuer(ISSUER_INFO).setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS256, SUPER_SECRET_KEY.getBytes(StandardCharsets.UTF_8)).compact();
		return token;
	}

	@Audit(message = "activate")
	@Operation(summary = "Activate", description = "Activació", tags = { "usuari" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Usuari.class)))) })
	@GetMapping(value = "/activate/{user}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Usuari> activateAccount(@PathVariable String user) throws ApiException {
		try {
			Usuari userValue = this.service.findByActivationCode(user);
			if (null != user) {
				long expire = userValue.getCreated().getTime() + (24 * 60 * 60 * 1000);
				if (expire > new Date().getTime()) {
					userValue.setActive(Boolean.TRUE);
					userValue.setActivationCode(null);
					Usuari usuari = this.service.save(userValue);
					usuari.setPassword(null);
					return ResponseEntity.ok(usuari);
				}
				throw new ApiException(HttpStatus.UNAUTHORIZED, "El seu token ha caducat");
			}
		} catch (Exception e) {
			LOG.debug("Token invalid or expired");
		}
		throw new ApiException(HttpStatus.UNAUTHORIZED, "No s'ha pogut activar el compte");
	}

	@Audit(message = "add user")
	@PutMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Usuari> createUser(@RequestBody Usuari usuari) throws ApiException {
		usuari.setPassword(EncryptionUtils.hash(usuari.getPassword()));
		Usuari actualUsuari = service.findByLogin(usuari.getLogin());
		if (null == actualUsuari) {
			usuari.setLogin(usuari.getLogin().toLowerCase());
			usuari.setCreated(new Date());
			usuari.setLoginCount(0);
			usuari.setLastLogin(0);
			usuari.setUpdated(new Date());
			usuari.setActivationCode(UUID.randomUUID().toString());
			usuari.setActive(Boolean.FALSE);
			String activationLink = this.createActivationLink(usuari.getActivationCode());
			Set<ContactEmail> emails = usuari.getEmails();
			Set<ContactSms> smss = usuari.getSmss();
			if (this.sendEmail(activationLink, new ArrayList<>(usuari.getEmails()).get(0).getEmail()).getStatusCode() == 202) {
				usuari = service.save(usuari);
				this.notificationService.saveUserContacts(usuari.getId(), smss, emails);
				return ResponseEntity.ok(usuari);
			}
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} else {
			// update, for now, unaccepted
			//			actualUsuari.setName(usuari.getName());
			//			actualUsuari.setSurname(usuari.getSurname());
			//			actualUsuari.setPassword(usuari.getPassword());
			//			actualUsuari.setUpdated(new Date());
			//			return ResponseEntity.ok(service.save(actualUsuari));
			throw new ApiException(HttpStatus.BAD_REQUEST, "L'usuari ja existeix al sistema.");
		}
	}

	@Audit(message = "all users")
	@GetMapping(value = { "/user/{id}", "/user" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Usuari>> getAllUsers(@PathVariable(value = "id", required = false) String id) {
		UUID uuid = parseId(id);
		List<Usuari> result;
		if (null != uuid) {
			result = Collections.singletonList(this.service.findById(uuid));
		} else {
			result = this.service.findAll();
		}
		for (Usuari usuari : result) {
			usuari.setPassword(null);
		}
		return ResponseEntity.ok(result);
	}

	public Response sendEmail(String activationLink, String email) {
		Email from = new Email("admin@smartstud.io");
		Email to = new Email(email);
		String subject = "CAEA Classificació d'Activitats Econòmiques Andorrana";
		try {
			com.sendgrid.Content emailContent = createEmailContent(activationLink);new com.sendgrid.Content("text/plain", "Clica el seguent enllaç per activar el teu compte " + activationLink);
			Mail mail = new Mail(from, subject, to, emailContent);
			Request request = new Request();
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			return sendGrid.api(request);
		} catch (Exception ex) {
			return null;
		}
	}

	private com.sendgrid.Content createEmailContent(String activationLink) throws Exception {
		String htmlString = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("plantilla_alta.html").toURI())));
		htmlString = htmlString.replace("$TOKEN", "Clica el seguent enllaç per activar el teu compte " + activationLink);
		return new com.sendgrid.Content("text/html", htmlString);
	}

	@SneakyThrows
	public String createActivationLink(String activationCode) {
		String userCode = URLEncoder.encode(activationCode, "UTF-8");
		return url + "/v1/activate/" + userCode;
	}
}
