package gr.digital.systems.crm.configuration;

import gr.digital.systems.crm.component.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

	private final JwtRequestFilter jwtRequestFilter;

	@Autowired
	public WebSecurityConfig(final JwtRequestFilter jwtRequestFilter) {
		this.jwtRequestFilter = jwtRequestFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
		http
				/* Disable CSRF because we're using JWT and REST APIs typically don't use session cookies */
				.csrf(AbstractHttpConfigurer::disable)
				/* Disable HTTP basic authentication */
				.httpBasic(AbstractHttpConfigurer::disable)
				/* Stateless session management */
				.securityContext(securityContext -> securityContext.requireExplicitSave(false))
				/* Authorization rules */
				.authorizeHttpRequests(
						authorizeRequests ->
								authorizeRequests
										.requestMatchers("/authenticate", "/api/**")
										/* Allow public access */
										.permitAll()
										.anyRequest()
										/* Protect all other routes */
										.authenticated())
				/* Add the JWT filter before UsernamePasswordAuthenticationFilter */
				.addFilterBefore(this.jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
