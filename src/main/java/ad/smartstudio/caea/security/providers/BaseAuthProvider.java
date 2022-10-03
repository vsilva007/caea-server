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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class BaseAuthProvider implements AuthenticationProvider {
	private UserService service;

	@Autowired
	public BaseAuthProvider(UserService service) {
		this.service = service;
	}

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		Usuari usuari = new Usuari();
		usuari.setLogin((String) auth.getPrincipal());
		usuari.setPassword(auth.getCredentials().toString());
		usuari = service.login(usuari);
		if (null != usuari && usuari.getActive()) {
			Usuari finalUsuari = usuari;
			List<GrantedAuthority> authorities = Collections.singletonList((GrantedAuthority) () -> finalUsuari.getRoleId().toString());
			return new UsernamePasswordAuthenticationToken(usuari, usuari.getPassword(), authorities);
		}
		throw new BadCredentialsException("Usuari no es pot logar");
	}

	@Override
	public boolean supports(Class<?> auth) {
		return auth.equals(UsernamePasswordAuthenticationToken.class);
	}
}
