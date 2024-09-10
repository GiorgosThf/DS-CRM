package gr.digital.systems.crm.exception;

import java.io.Serial;

public class DatabaseException extends RuntimeException {

	@Serial private static final long serialVersionUID = 1L;

	public DatabaseException(final String message) {
		super(message);
	}

	public DatabaseException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
