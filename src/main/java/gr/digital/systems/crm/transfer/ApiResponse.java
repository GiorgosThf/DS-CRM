package gr.digital.systems.crm.transfer;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ApiResponse<T> {

	T data;
	ApiError apiError;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss.SSS")
	LocalDateTime timestamp = LocalDateTime.now();
}
