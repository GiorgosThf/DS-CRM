package gr.digital.systems.crm.configuration;

import gr.digital.systems.crm.component.NginxRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<NginxRequestFilter> customNginxRequestFilter() {
		FilterRegistrationBean<NginxRequestFilter> registrationBean = new FilterRegistrationBean<>();

		registrationBean.setFilter(new NginxRequestFilter());
		registrationBean.addUrlPatterns("/api/*"); // Apply the filter to specific URL patterns

		return registrationBean;
	}
}
