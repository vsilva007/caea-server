package ad.smartstudio.caea.security.providers;

import ad.smartstudio.caea.model.entity.Usuari;
import ad.smartstudio.caea.security.utils.PasswordEncoder;
import ad.smartstudio.caea.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class TokenRefreshAuthProvider implements AuthenticationProvider {
	private final UserService service;

	@Autowired
	public TokenRefreshAuthProvider(UserService service) {
		this.service = service;
	}

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		Usuari usuari = new Usuari();
		usuari.setLogin(auth.getName());
		usuari.setPassword(PasswordEncoder.getInstance().encode(auth.getCredentials().toString()));
		usuari = service.login(usuari);
//		if (null != usuari && null != usuari.getRefreshToken() && auth.getCredentials().toString().contains("/refresh")) {
//			List<GrantedAuthority> authorities = new ArrayList<>(); // TODO implementar authorities
//			return new UsernamePasswordAuthenticationToken(usuari, null, authorities);
//		}
		throw new BadCredentialsException("Usuari no es pot logar");
	}

	@Override
	public boolean supports(Class<?> auth) {
		return auth.equals(UsernamePasswordAuthenticationToken.class);
	}
}
