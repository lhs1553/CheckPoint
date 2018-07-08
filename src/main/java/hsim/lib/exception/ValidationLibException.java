/**
 *
 */
package hsim.lib.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Create by hsim on 2018. 1. 25.
 */
@Getter
public class ValidationLibException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private HttpStatus statusCode;

    public ValidationLibException(HttpStatus statusCode) {
        super();
        this.statusCode = statusCode;
    }

    public ValidationLibException(String message) {
        super(message);
    }

    public ValidationLibException(Throwable throwable) {
        super(throwable);
    }

    public ValidationLibException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ValidationLibException(HttpStatus statusCode, Throwable throwable) {
        super(throwable);
        this.statusCode = statusCode;
    }

    public ValidationLibException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public ValidationLibException(HttpClientErrorException e) {
        super(e.getMessage());
        this.statusCode = e.getStatusCode();
    }

    public ValidationLibException(String message, HttpStatus statusCode, Throwable throwable) {
        super(message, throwable);
        this.statusCode = statusCode;
    }
}
