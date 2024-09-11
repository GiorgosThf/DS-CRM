package gr.digital.systems.crm.configuration;

import com.google.common.collect.ImmutableMap;
import gr.digital.systems.crm.utils.XMLUtils;
import java.util.Map;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.w3c.dom.Document;

@Configuration
public class AllureConfig {

	private final String siteEnvironment;
	private final String siteName;

	@Autowired
	public AllureConfig(final EnvironmentPropertiesConfig environmentPropertiesConfig) {
		this.siteEnvironment = environmentPropertiesConfig.getEnvironment();
		this.siteName = environmentPropertiesConfig.getSite();
	}

	private static final String ALLURE_DIR_RESULTS =
			System.getProperty(
					"allure.results.directory", SystemUtils.USER_DIR + "/target/allure-results/");

	@Bean
	public Document setAllureEnvironment() {
		/* Create map containing the information to show in allure environment section */
		ImmutableMap.Builder<String, String> mapBuilder =
				new ImmutableMap.Builder<String, String>()
						.put("Environment", this.siteEnvironment)
						.put("Site", this.siteName)
						.put("URL", "http://localhost:8081");

		Map<String, Map<String, String>> environmentXML =
				new ImmutableMap.Builder<String, Map<String, String>>()
						.put("environment", mapBuilder.build())
						.build();

		/* Create environment.xml file with the information regarding environment */
		return XMLUtils.createXmlDocument(ALLURE_DIR_RESULTS, "/environment.xml", environmentXML);
	}
}
