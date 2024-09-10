package gr.digital.systems.crm.component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class NginxRequestFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		final var httpRequest = (HttpServletRequest) request;
		final var httpResponse = (HttpServletResponse) response;

		// Check for the custom header "X-Forwarded-From-Nginx"
		String nginxHeader = httpRequest.getHeader("X-Forwarded-From-Nginx");

		if (nginxHeader == null || !nginxHeader.equals("true")) {
			// If the header is missing or incorrect, return a 403 Forbidden response
			httpResponse.sendError(
					HttpServletResponse.SC_FORBIDDEN, "Access denied: Requests must go through NGINX.");
			return;
		}

		// Proceed with the request if the header is present and correct
		chain.doFilter(request, response);
	}
}
