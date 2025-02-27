package pl.kowalecki.dietplanner.exception;

public class ClientErrorException extends RuntimeException {
    private final int statusCode;
    private final String requestedUrl;

    public ClientErrorException(String message, int statusCode, String requestedUrl) {
        super(message);
        this.statusCode = statusCode;
        this.requestedUrl = requestedUrl;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getRequestedUrl() {
        return requestedUrl;
    }
}