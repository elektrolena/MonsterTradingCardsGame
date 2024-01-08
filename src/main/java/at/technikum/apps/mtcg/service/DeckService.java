package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseCardRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DeckService {

    private final DatabaseCardRepository cardRepository;

    public DeckService(DatabaseCardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Optional<List<Card>> getDeck(User user) throws SQLException {
        return this.cardRepository.getDeck(user.getId());
    }

    public int updateDeck(User user, Card[] cards) throws SQLException {
        if(cards.length != 4) {
            return 400;
        }

        for(Card card : cards) {
            if(this.cardRepository.checkForOwnership(card.getId(), user.getId()).isEmpty()) {
                return 403;
            }
        }

        for(Card card : cards) {
            this.cardRepository.addCardToDeck(card.getId());
        }
        return 200;
    }
}
