package ua.test.exceptions;

/**
 * This class describe exceptions entity if cannot proccess process data
 *
 * @author victorzagnitko
 */
public class BusinessLogicException extends Exception {
    private String message;

    public BusinessLogicException(String message) {
        super(message);
        this.message = message;
    }

    public BusinessLogicException(Exception exc) {
        super(exc);
        this.message = exc.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
