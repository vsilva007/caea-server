package ad.smartstudio.caea.security.filters;

import ad.smartstudio.caea.model.entity.Usuari;
import ad.smartstudio.caea.security.TokenAuthenticationService;
import ad.smartstudio.caea.security.utils.AccountCredentials;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static ad.smartstudio.caea.security.utils.Constants.*;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {
	private static final Log LOG = LogFactory.getLog(JWTLoginFilter.class);
	private TokenAuthenticationService tokenAuthenticationService;

	public JWTLoginFilter(String url, AuthenticationManager authManager, TokenAuthenticationService tokenAuthenticationService) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
		this.tokenAuthenticationService = tokenAuthenticationService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) {
		AccountCredentials creds;
		try {
			if (!req.getInputStream().isFinished()) {
				LOG.info("[JWTLoginFilter] - Auth Header: " + req.getHeader("Authorization"));
				creds = new ObjectMapper().readValue(req.getInputStream(), AccountCredentials.class);
				if (creds != null) {
					return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword(), new ArrayList<>()));
				}
			}
		} catch (IOException e) {
			LOG.error(e.getMessage());
			LOG.debug(e.getStackTrace());
		}
		LOG.info("[JWTLoginFilter] - Login without credentials on POST body");
		return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken("", "", new ArrayList<>()));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) {
		Usuari usuari = (Usuari) auth.getPrincipal();
		String token = tokenAuthenticationService.addAuthentication(usuari);
		res.addHeader(HEADER_AUTHORIZACION_KEY, token);
	}
}