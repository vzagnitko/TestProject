package ua.test.controllers.advices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.test.exceptions.HttpRequestException;
import ua.test.exceptions.RestException;
import ua.test.wrapper.ErrorResponseWrapper;

/**
 * @author Victor Zagnitko on 10.10.2014.
 */
@RestController
@ControllerAdvice
public class GeneralExceptionHandlerController {

    private static final Logger LOG = LoggerFactory.getLogger(GeneralExceptionHandlerController.class);

    /**
     * Except IllegalArgumentException or IllegalStateException
     *
     * @param exc object which contains exceptions information
     * @return response of error
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ErrorResponseWrapper> errorArgumentHandler(RuntimeException exc) {
        LOG.error("{}", exc);
        return new ResponseEntity<>(new ErrorResponseWrapper(HttpStatus.BAD_REQUEST.value(),
                exc.getMessage()), null, HttpStatus.BAD_REQUEST);
    }

    /**
     * Except RestException
     *
     * @param exc object which contains exceptions information
     * @return response of error
     */
    @ExceptionHandler(value = HttpRequestException.class)
    public ResponseEntity<ErrorResponseWrapper> errorHttpRequestExceptionHandler(HttpRequestException exc) {
        LOG.error("{}", exc);
        return new ResponseEntity<>(new ErrorResponseWrapper(exc.getStatus().value(), exc.getMessage()), null, exc.getStatus());
    }

    /**
     * Except RestException
     *
     * @param exc object which contains exceptions information
     * @return response of error
     */
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = RestException.class)
    public ResponseEntity<ErrorResponseWrapper> errorRestExceptionHandler(RestException exc) {
        LOG.error("{}", exc);
        return new ResponseEntity<>(new ErrorResponseWrapper(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exc.getMessage()), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Except Throwable
     *
     * @param exc object which contains exceptions information
     * @return response of error
     */
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<ErrorResponseWrapper> errorExceptionHandler(Throwable exc) {
        LOG.error("{}", exc);
        return new ResponseEntity<>(new ErrorResponseWrapper(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exc.getMessage()), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}