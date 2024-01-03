package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

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

    protected Card[] getCardsFromBody(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();
        Card[] cards = null;
        try {
            JsonNode jsonNode = objectMapper.readTree(request.getBody());
            cards = objectMapper.treeToValue(jsonNode, Card[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return cards;
    }

    protected String convertObjectToJson(Object object) {
        ObjectMapper objectmapper = new ObjectMapper();

        String taskJson = null;
        try {
            taskJson = objectmapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return taskJson;
    }

    protected <T> String convertObjectListToJson(List<T> objectList) {
        ObjectMapper objectMapper = new ObjectMapper();

        String json = null;
        try {
            json = objectMapper.writeValueAsString(objectList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return json;
    }


    protected Response createResponse(HttpContentType contentType, HttpStatus status, String body) {
        Response response = new Response();

        response.setStatus(status);
        response.setContentType(contentType);
        response.setBody(body);

        return response;
    }

    protected Optional<User> checkForAuthorizedRequest(Request request, UserService userService) {
        if(request.getAuthorizationToken() == null) {
            return Optional.empty();
        }

        return userService.findWithToken(request.getAuthorizationToken());
    }

    protected String extractLastRoutePart(String route) {
        String[] routeParts = route.split("/");
        return routeParts[2];
    }
}
