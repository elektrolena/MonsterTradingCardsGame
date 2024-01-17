package at.technikum.apps.mtcg.exceptions;

import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;

public class HttpStatusException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final HttpContentType httpContentType;
    private final ExceptionMessage exceptionMessage;
    public HttpStatusException(HttpStatus httpStatus, HttpContentType httpContentType, ExceptionMessage exceptionMessage) {
        this.httpStatus = httpStatus;
        this.httpContentType = httpContentType;
        this.exceptionMessage = exceptionMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public HttpContentType getHttpContentType() {
        return httpContentType;
    }

    public ExceptionMessage getHttpStatusMessage() {
        return exceptionMessage;
    }
}
