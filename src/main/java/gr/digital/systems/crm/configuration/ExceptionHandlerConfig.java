package gr.digital.systems.crm.configuration;

import gr.digital.systems.crm.exception.CrmException;
import gr.digital.systems.crm.transfer.ApiError;
import gr.digital.systems.crm.transfer.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ExceptionHandlerConfig {
	private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerConfig.class);

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ApiResponse<Void>> handleIllegalStateException(
			final Throwable ex, final WebRequest webRequest) {
		LOG.error("IllegalStateException caught: {}. Cause: {}", ex.getMessage(), ex.getStackTrace());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(
						ApiResponse.<Void>builder()
								.apiError(
										ApiError.builder()
												.description(ex.getMessage())
												.httpStatus(HttpStatus.UNAUTHORIZED.value())
												.path(webRequest.getDescription(false))
												.build())
								.build());
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ApiResponse<Void>> handleNullPointerException(
			final Throwable ex, final WebRequest webRequest) {
		LOG.error("NullPointerException caught: {}. Cause: {}", ex.getMessage(), ex.getStackTrace());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(
						ApiResponse.<Void>builder()
								.apiError(
										ApiError.builder()
												.description(ex.getMessage())
												.httpStatus(HttpStatus.BAD_REQUEST.value())
												.path(webRequest.getDescription(true))
												.build())
								.build());
	}

	@ExceptionHandler(CrmException.class)
	protected ResponseEntity<ApiResponse<Void>> handleTestAutomationException(
			final CrmException ex, final WebRequest webRequest) {
		LOG.error("CrmException caught: {}. Cause: {}", ex.getMessage(), ex.getStackTrace());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(
						ApiResponse.<Void>builder()
								.apiError(
										ApiError.builder()
												.description(ex.getMessage())
												.httpStatus(HttpStatus.NOT_FOUND.value())
												.path(webRequest.getDescription(false))
												.build())
								.build());
	}

	@ExceptionHandler(AssertionError.class)
	protected ResponseEntity<ApiResponse<Void>> handleAssertionError(
			final AssertionError ex, final WebRequest webRequest) {
		LOG.error("AssertionError caught: {}. Cause: {}", ex.getMessage(), ex.getStackTrace());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(
						ApiResponse.<Void>builder()
								.apiError(
										ApiError.builder()
												.description(ex.getMessage())
												.httpStatus(HttpStatus.NOT_FOUND.value())
												.path(webRequest.getDescription(false))
												.build())
								.build());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(
			final MethodArgumentNotValidException ex, final WebRequest webRequest) {
		LOG.error(
				"MethodArgumentNotValidException caught: {}. Cause: {}",
				ex.getMessage(),
				ex.getStackTrace());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(
						ApiResponse.<Void>builder()
								.apiError(
										ApiError.builder()
												.description(ex.getMessage())
												.httpStatus(HttpStatus.NOT_FOUND.value())
												.path(webRequest.getDescription(false))
												.build())
								.build());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	protected ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(
			final HttpMessageNotReadableException ex, final WebRequest webRequest) {
		LOG.error(
				"HttpMessageNotReadableException caught: {}. Cause: {}",
				ex.getMessage(),
				ex.getStackTrace());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(
						ApiResponse.<Void>builder()
								.apiError(
										ApiError.builder()
												.description(ex.getMessage())
												.httpStatus(HttpStatus.BAD_REQUEST.value())
												.path(webRequest.getDescription(false))
												.build())
								.build());
	}
}
