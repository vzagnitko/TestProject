package ua.test.repository;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ua.test.exceptions.HttpRequestException;

import javax.annotation.Nonnull;

/**
 * This class contains main methods to storage information in repository
 *
 * @author victorzagnitko on 15.09.16.
 */
public abstract class AbstractRepository {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractRepository.class);

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Send request to external repository
     *
     * @param uri        to send request
     * @param httpMethod method which use to send request
     * @param entity     to POST request
     * @param objectType response type
     * @param <T>        generic type of response type
     * @return response entity
     */
    protected <T> ResponseEntity<T> sendRequest(@Nonnull String uri, @Nonnull HttpMethod httpMethod,
                                                Object entity, @Nonnull Class<T> objectType) {
        LOG.info("Send request uri {}, method {}, type {}", uri, httpMethod, objectType);
        HttpEntity<?> httpEntity = new HttpEntity<>(entity, initHttpHeaders());
        ResponseEntity<T> response;
        try {
            response = restTemplate.exchange(uri, httpMethod, httpEntity, objectType);
        } catch (HttpStatusCodeException exc) {
            LOG.error("Error take response {}", exc);
            throw new HttpRequestException(exc.getStatusText(), exc.getStatusCode());
        }
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new HttpRequestException("Cannot receive http 200", response.getStatusCode());
        }
        LOG.info("Success receive response");
        return response;
    }

    /**
     * Init header for request
     *
     * @return inited HttpHeaders
     */
    private HttpHeaders initHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON_UTF8));
        return headers;
    }

}
