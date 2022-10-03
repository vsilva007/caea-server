package ad.smartstudio.caea.security.filters;

import ad.smartstudio.caea.model.entity.Usuari;
import ad.smartstudio.caea.security.utils.Authority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.UUID;

import static ad.smartstudio.caea.security.utils.Constants.*;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	private static final Log LOG = LogFactory.getLog(JWTAuthorizationFilter.class);

	public JWTAuthorizationFilter(AuthenticationManager authManager) {
		super(authManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
		String header = req.getHeader(HEADER_AUTHORIZACION_KEY);
		if (header == null || !header.startsWith(TOKEN_BEARER_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}
		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String tokenStr = request.getHeader(HEADER_AUTHORIZACION_KEY).replace(TOKEN_BEARER_PREFIX, "");
		try {
			Claims token = Jwts.parser().setSigningKey(SUPER_SECRET_KEY.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(tokenStr).getBody();
			LOG.trace("[JWTAuthorizationFilter] - User parsed from token: " + token.getSubject());
			return new UsernamePasswordAuthenticationToken(parseUserFromToken(token), null, Collections.singletonList(new Authority(String.valueOf(token.get("role")))));
		} catch (Exception e) {
			LOG.error("[JWTAuthorizationFilter] - Error parsing Token ");
			LOG.error(e.getMessage());
		}
		return null;
	}

	private Usuari parseUserFromToken(Claims token) {
		return new Usuari(UUID.fromString(token.getSubject()));
	}
}
