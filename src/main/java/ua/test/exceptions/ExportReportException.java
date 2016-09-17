package ua.test.exceptions;

/**
 * This class describe exceptions entity if cannot export data
 *
 * @author victorzagnitko
 */
public class ExportReportException extends Exception {

    private String message;

    public ExportReportException(String message) {
        super(message);
        this.message = message;
    }

    public ExportReportException(Exception exc) {
        super(exc);
        this.message = exc.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
