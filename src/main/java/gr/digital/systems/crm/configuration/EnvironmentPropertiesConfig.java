package gr.digital.systems.crm.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Getter
@Order(0)
public class EnvironmentPropertiesConfig {

	@Value("${site.environment}")
	private String environment;

	@Value("${site.name}")
	private String site;

	@Value("${jwt.key}")
	private String jwtKey;
}
