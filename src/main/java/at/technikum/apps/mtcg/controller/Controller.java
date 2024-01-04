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
import com.fasterxml.jackson.databind.node.ObjectNode;

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

    protected static String convertStringToJson(String inputString) {
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode jsonNode = objectMapper.createObjectNode();

        String[] lines = inputString.split("\n");
        for (String line : lines) {
            String[] parts = line.split(": ");
            if (parts.length == 2) {
                String key = parts[0].trim();
                String value = parts[1].trim();
                jsonNode.put(key, value);
            }
        }

        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: change outputs to required outputs
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

    public <T> String convertObjectListToPlainText(List<T> objectList) {
        StringBuilder responseBody = new StringBuilder();

        if (objectList == null || objectList.isEmpty()) {
            return "The request was fine, but the deck doesn't have any cards.";
        }

        ObjectMapper objectMapper = new ObjectMapper();

        Object[] objectsArray = objectList.toArray();
        try {
            JsonNode jsonNode = objectMapper.valueToTree(objectsArray);

            Card[] cards = objectMapper.treeToValue(jsonNode, Card[].class);

            for (Card card : cards) {
                responseBody.append("  CardId: ").append(card.getId()).append("\r\n");
                responseBody.append("    Name: ").append(card.getName()).append("\r\n");
                responseBody.append("    Damage: ").append(card.getDamage()).append("\r\n");
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return responseBody.toString();
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
