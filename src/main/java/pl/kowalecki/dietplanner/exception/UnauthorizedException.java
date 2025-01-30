package pl.kowalecki.dietplanner.exception;

import org.springframework.web.reactive.function.client.WebClientException;

public class UnauthorizedException extends WebClientException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

}
