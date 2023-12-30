package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Controller {

    public abstract boolean supports(String route);

    public abstract Response handle(Request request);

    protected User getUserFromBody(Request request) {
        ObjectMapper objectmapper = new ObjectMapper();
        User user = null;
        try {
            user = objectmapper.readValue(request.getBody(), User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    protected String convertUserObjectToJson(User user) {
        ObjectMapper objectmapper = new ObjectMapper();

        String taskJson = null;
        try {
            taskJson = objectmapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return taskJson;
    }

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
