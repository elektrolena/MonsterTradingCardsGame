package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.repository.DatabaseCardRepository;
import at.technikum.server.http.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

public class PackageService {

    private final DatabaseCardRepository databaseCardRepository;

    public PackageService(DatabaseCardRepository databaseCardRepository) {
        this.databaseCardRepository = databaseCardRepository;
    }

    public boolean save(Request request) {
        Card[] cards = getCardsFromBody(request);

        for(Card card : cards) {
            if(this.databaseCardRepository.findWithId(card.getId()).isPresent()) {
                return false;
            }
        }

        String packageId = UUID.randomUUID().toString();

        for (Card card : cards) {
            card.setPackageId(packageId);
            this.databaseCardRepository.save(card);
        }

        return true;
    }
    private Card[] getCardsFromBody(Request request) {
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
}
