package gr.digital.systems.crm.component;

import gr.digital.systems.crm.configuration.EnvironmentPropertiesConfig;
import gr.digital.systems.crm.exception.CrmException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private final Key secretKey;

	@Autowired
	public JwtRequestFilter(final EnvironmentPropertiesConfig environmentPropertiesConfig) {
		this.secretKey =
				Keys.hmacShaKeyFor(
						environmentPropertiesConfig.getJwtKey().getBytes(StandardCharsets.UTF_8));
	}

	private static final Logger LOG = LoggerFactory.getLogger(JwtRequestFilter.class);

	@Override
	protected void doFilterInternal(
			@NonNull final HttpServletRequest request,
			@NonNull final HttpServletResponse response,
			@NonNull final FilterChain chain) {

		/* Extrac token from header */
		var jwtToken = request.getHeader("X-JWT-Token");

		if (jwtToken == null) {
			this.sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Missing JWT token");
			return;
		}

		try {
			/* Parse and validate the JWT */
			var claims =
					Jwts.parserBuilder()
							/* Initialized key */
							.setSigningKey(this.secretKey)
							.build()
							.parseClaimsJws(jwtToken)
							.getBody();

			/* Log details about the JWT */
			LOG.info(
					"Valid JWT: Issuer={}, Subject={}, Expiration={}",
					claims.getIssuer(),
					claims.getSubject(),
					claims.getExpiration());

			chain.doFilter(request, response); // Proceed with the request

		} catch (final Exception e) {
			LOG.warn("Invalid JWT token from IP: {}. Error: {}", request.getRemoteAddr(), e.getMessage());
			this.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
		}
	}

	private void sendErrorResponse(
			final HttpServletResponse response, final Integer responseStatus, final String message) {
		try {
			response.sendError(responseStatus, message);
		} catch (final Exception e) {
			throw new CrmException("Internal Server Error");
		}
	}
}
