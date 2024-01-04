package at.technikum.apps.mtcg.parsing;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.server.http.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public class JsonParser {
    public User getUserFromBody(Request request) {
        ObjectMapper objectmapper = new ObjectMapper();
        User user = null;
        try {
            user = objectmapper.readValue(request.getBody(), User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    public Card[] getCardsFromBody(Request request) {
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

    public String getUserCredentials(User user) {
        return convertStringToJson(createUserStatsString(user));
    }

    public String getUserData(User user) {
        return convertStringToJson(createUserDataString(user));
    }

    public String getUserStats(User user) {
        return convertStringToJson(createUserStatsString(user));
    }

    public String getCards(List<Card> cards) {
        return convertStringToJson(createCardArrayString(cards));
    }

    public String getCardsPlain(List<Card> cards) {
        return createCardArrayString(cards);
    }

    private String createUserCredentialsString(User user) {
        return "Username: " + user.getUsername() + "\nPassword: " + user.getPassword();
    }

    private String createUserDataString(User user) {
        return "Username: " + user.getUsername() + "\nBio: " + user.getBio() + "\nImage: " + user.getImage();
    }

    private String createUserStatsString(User user) {
        return "Username: " + user.getUsername() + "\nElo: " + user.getElo() + "\nWins: " + user.getWins() + "\nLosses: " + user.getLosses();
    }

    private String createCardArrayString(List<Card> cards) {
        StringBuilder cardString = new StringBuilder();

        for (Card card : cards) {
            cardString.append("CardId: ").append(card.getId()).append("\n");
            cardString.append("Name: ").append(card.getName()).append("\n");
            cardString.append("Damage: ").append(card.getDamage()).append("\n\n");
        }

        return cardString.toString();
    }

    private String convertStringToJson(String inputString) {
        ObjectMapper objectMapper = new ObjectMapper();

        ArrayNode jsonArrayNode = objectMapper.createArrayNode();

        String[] objectBlocks = inputString.split("\n\n");

        for (String objectBlock : objectBlocks) {
            ObjectNode jsonObjectNode = objectMapper.createObjectNode();

            String[] lines = objectBlock.split("\n");
            for (String line : lines) {
                String[] parts = line.split(": ");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    jsonObjectNode.put(key, value);
                }
            }

            jsonArrayNode.add(jsonObjectNode);
        }

        try {
            String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonArrayNode);

            jsonString = jsonString.replaceAll(", \\{", ",\n{");

            return jsonString;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
