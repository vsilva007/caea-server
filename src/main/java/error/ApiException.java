package error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ApiException extends Throwable {
	private HttpStatus status;
	private String message;
	private List<String> errors;

	public ApiException(HttpStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
}
