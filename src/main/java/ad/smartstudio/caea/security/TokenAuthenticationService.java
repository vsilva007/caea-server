package ad.smartstudio.caea.security;

import ad.smartstudio.caea.model.entity.Usuari;
import ad.smartstudio.caea.util.EncryptionUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static ad.smartstudio.caea.security.utils.Constants.*;

@Service
public class TokenAuthenticationService {
	private static final Log LOG = LogFactory.getLog(TokenAuthenticationService.class);

	public String addAuthentication(Usuari usuari) {
		String token;
		LOG.info("[TokenAuthenticationService] - login for user: " + usuari.getLogin());
		Map<String, Object> map = new HashMap<>();
		map.put("role", usuari.getRoleId());
		token = Jwts.builder().setClaims(map).setSubject(usuari.getId().toString()).setIssuedAt(new Date()).setIssuer(ISSUER_INFO).setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS256, SUPER_SECRET_KEY.getBytes(StandardCharsets.UTF_8)).compact();
		return token;
	}
}
