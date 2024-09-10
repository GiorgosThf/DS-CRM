package gr.digital.systems.crm.exception;

import java.io.Serial;

public class NginxException extends RuntimeException {

	@Serial private static final long serialVersionUID = 1L;

	public NginxException(final String message) {
		super(message);
	}

	public NginxException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
