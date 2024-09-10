package gr.digital.systems.crm.exception;

import java.io.Serial;

public class CrmException extends RuntimeException {

	@Serial private static final long serialVersionUID = 1L;

	public CrmException(final String message) {
		super(message);
	}

	public CrmException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
