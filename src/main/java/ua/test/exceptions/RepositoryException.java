package ua.test.exceptions;

/**
 * This class describe exceptions entity if cannot receive correct response from the repository
 *
 * @author victorzagnitko
 */
public class RepositoryException extends Exception {

    private String message;

    public RepositoryException(String message) {
        super(message);
        this.message = message;
    }

    public RepositoryException(Exception exc) {
        super(exc);
        this.message = exc.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }

}
