package ua.test.exceptions;

import org.springframework.http.HttpStatus;

/**
 * This class describe exceptions entity if cannot receive correct response from the server
 *
 * @author vzagnitko
 */
public class HttpRequestException extends RuntimeException {

    private String message;

    private HttpStatus status;

    public HttpRequestException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }


}
