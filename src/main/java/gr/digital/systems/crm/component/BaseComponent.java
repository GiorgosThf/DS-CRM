package gr.digital.systems.crm.component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseComponent {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@PostConstruct
	public void init() {
		logger.trace("Loaded {} ", getClass());
	}

	@PreDestroy
	public void destroy() {
		logger.trace("{} is about to get Destroyed", getClass().getName());
	}
}
