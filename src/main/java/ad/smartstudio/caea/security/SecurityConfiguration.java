package ad.smartstudio.caea.security;

import ad.smartstudio.caea.security.filters.JWTAuthorizationFilter;
import ad.smartstudio.caea.security.providers.BaseAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Created by cpaparuz on 27/06/2017.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	private final BaseAuthProvider baseAuthProvider;
	protected TokenAuthenticationService tokenAuthenticationService;

	@Autowired
	public SecurityConfiguration(BaseAuthProvider baseAuthProvider, TokenAuthenticationService tokenAuthenticationService) {
		this.baseAuthProvider = baseAuthProvider;
		this.tokenAuthenticationService = tokenAuthenticationService;
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.setAllowedOrigins(Arrays.asList("http://localhost:4202", "https://caea.io"));
		config.setAllowedMethods(Arrays.asList("POST", "OPTIONS", "GET", "DELETE", "PUT"));
		config.setAllowedHeaders(Arrays.asList("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization", "RefreshToken"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(baseAuthProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests();
		http.authorizeRequests().antMatchers(HttpMethod.PUT, "/v1/user").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/v1/user").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/v1/authenticate").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/v1/activity").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/v1/activate/**").permitAll();
		http.authorizeRequests().anyRequest().authenticated().and().addFilter(new JWTAuthorizationFilter(authenticationManager()));
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
}
