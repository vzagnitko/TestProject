package ua.test.exceptions;

/**
 * This class describe exceptions entity if cannot receive correct response from the repository
 *
 * @author vzagnitko
 */
public class RestException extends RuntimeException {

    private String message;

    public RestException(String message) {
        super(message);
        this.message = message;
    }

    public RestException(Exception exc) {
        super(exc);
        this.message = exc.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }


}
