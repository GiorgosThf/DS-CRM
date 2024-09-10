package gr.digital.systems.crm.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.addServersItem(
						new Server().url("http://localhost:8081")) // HTTP access (direct backend access)
				.addServersItem(new Server().url("https://localhost")) // HTTPS via NGINX
				.info(
						new Info()
								.title("DS CRM App API")
								.description("This is the API documentation for DS CRM App"));
	}

	@Bean
	public GroupedOpenApi publicApi() {
		return GroupedOpenApi.builder().group("public").pathsToMatch("/api/**").build();
	}
}
