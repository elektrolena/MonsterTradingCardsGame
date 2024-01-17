package at.technikum.server.http;

public enum HttpStatus {
    OK(200, "OK"),
    CREATED(201, "Created"),
    NO_CONTENT(204, "No Content"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED_ACCESS(401, "Unauthorized Access"),
    FORBIDDEN(403, "Forbidden Access"),
    NOT_FOUND(404, "Not Found"),
    ALREADY_EXISTS(409, "Already exists"),
    INTERNAL_ERROR(500, "Internal Server Error");

    private final int code;
    private final String message;

    HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
