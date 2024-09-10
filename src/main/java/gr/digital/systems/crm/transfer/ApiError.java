package gr.digital.systems.crm.transfer;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ApiError {
	String description;
	Integer httpStatus;
	String path;
}
