package gr.digital.systems.crm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // Use the test profile to load application-test.properties
class DsCrmApplicationTests {

	@Test
	void contextLoads() {
		Assertions.assertTrue(true);
	}
}
