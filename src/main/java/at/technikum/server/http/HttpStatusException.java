package at.technikum.server.http;

public class HttpStatusException extends RuntimeException {
    private HttpStatus httpStatus;
    private HttpContentType httpContentType;
    private HttpStatusMessage httpStatusMessage;
    public HttpStatusException(HttpStatus httpStatus, HttpContentType httpContentType, HttpStatusMessage httpStatusMessage) {
        this.httpStatus = httpStatus;
        this.httpContentType = httpContentType;
        this.httpStatusMessage = httpStatusMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpContentType getHttpContentType() {
        return httpContentType;
    }

    public void setHttpContentType(HttpContentType httpContentType) {
        this.httpContentType = httpContentType;
    }

    public HttpStatusMessage getHttpStatusMessage() {
        return httpStatusMessage;
    }

    public void setHttpStatusMessage(HttpStatusMessage httpStatusMessage) {
        this.httpStatusMessage = httpStatusMessage;
    }
}
