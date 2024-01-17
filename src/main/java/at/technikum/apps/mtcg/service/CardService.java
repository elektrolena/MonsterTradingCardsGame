package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.exceptions.ExceptionMessage;
import at.technikum.apps.mtcg.exceptions.HttpStatusException;
import at.technikum.apps.mtcg.repository.DatabaseCardRepository;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;

import java.util.List;

public class CardService {
    private final AuthorizationService authorizationService;
    private final DatabaseCardRepository databaseCardRepository;

    public CardService(AuthorizationService authorizationService, DatabaseCardRepository databaseCardRepository) {
        this.authorizationService = authorizationService;
        this.databaseCardRepository = databaseCardRepository;
    }

    public List<Card> getAllCardsFromUser(String authorizationToken) {
        User user = this.authorizationService.getLoggedInUser(authorizationToken);
        List<Card> cards = this.databaseCardRepository.getCardsFromUser(user.getId());
        if(cards.isEmpty()) {
            throw new HttpStatusException(HttpStatus.NO_CONTENT, HttpContentType.TEXT_PLAIN, ExceptionMessage.NO_CONTENT_CARD);
        }
        return cards;
    }
}
