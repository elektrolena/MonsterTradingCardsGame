package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.exceptions.ExceptionMessage;
import at.technikum.apps.mtcg.exceptions.HttpStatusException;
import at.technikum.apps.mtcg.repository.DatabaseCardRepository;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;

import java.util.List;

public class DeckService {

    private final AuthorizationService authorizationService;
    private final DatabaseCardRepository cardRepository;

    public DeckService(AuthorizationService authorizationService, DatabaseCardRepository cardRepository) {
        this.authorizationService = authorizationService;
        this.cardRepository = cardRepository;
    }

    public List<Card> getDeck(String authorizationToken) {
        User user = this.authorizationService.getLoggedInUser(authorizationToken);

        List<Card> deck = this.cardRepository.getDeck(user.getId());
        if(deck.isEmpty()) {
            throw new HttpStatusException(HttpStatus.NO_CONTENT, HttpContentType.TEXT_PLAIN, ExceptionMessage.NO_CONTENT_DECK);
        }

        return deck;
    }

    public void updateDeck(String authorizationToken, List<Card> cards) {
        User user = this.authorizationService.getLoggedInUser(authorizationToken);
        if(cards.size() != 4) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, HttpContentType.TEXT_PLAIN, ExceptionMessage.BAD_REQUEST_DECK);
        }

        for(Card card : cards) {
            if(this.cardRepository.checkForOwnership(card.getId(), user.getId()).isEmpty()) {
                throw new HttpStatusException(HttpStatus.FORBIDDEN, HttpContentType.TEXT_PLAIN, ExceptionMessage.FORBIDDEN_DECK);
            }
        }

        for(Card card : cards) {
            this.cardRepository.addCardToDeck(card.getId());
        }
    }
}
