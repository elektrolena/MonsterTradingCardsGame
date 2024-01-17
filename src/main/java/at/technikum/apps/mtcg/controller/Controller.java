package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public abstract class Controller {

    protected final JsonParser parser;

    protected Controller(JsonParser parser) {
        this.parser = parser;
    }

    public abstract boolean supports(String route);

    public abstract Response handle(Request request);

    protected Response createResponse(HttpContentType contentType, HttpStatus status, String body) {
        Response response = new Response();

        response.setStatus(status);
        response.setContentType(contentType);
        response.setBody(body);

        return response;
    }

    protected String extractLastRoutePart(String route) {
        String[] routeParts = route.split("/");
        return routeParts[2];
    }
}
