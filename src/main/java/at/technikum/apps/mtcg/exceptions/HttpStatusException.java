package at.technikum.apps.mtcg.exceptions;

import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;

public class HttpStatusException extends RuntimeException {
    private HttpStatus httpStatus;
    private HttpContentType httpContentType;
    private ExceptionMessage exceptionMessage;
    public HttpStatusException(HttpStatus httpStatus, HttpContentType httpContentType, ExceptionMessage exceptionMessage) {
        this.httpStatus = httpStatus;
        this.httpContentType = httpContentType;
        this.exceptionMessage = exceptionMessage;
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

    public ExceptionMessage getHttpStatusMessage() {
        return exceptionMessage;
    }

    public void setHttpStatusMessage(ExceptionMessage exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
