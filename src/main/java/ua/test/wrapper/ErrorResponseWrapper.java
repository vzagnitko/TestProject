package ua.test.wrapper;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * This class use to wrap error response
 *
 * @author vzagnitko
 */
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class ErrorResponseWrapper implements Serializable {

    private int status;

    private String message;

    public ErrorResponseWrapper() {

    }

    public ErrorResponseWrapper(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
