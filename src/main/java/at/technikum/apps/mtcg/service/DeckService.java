package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseCardRepository;

import java.util.List;
import java.util.Optional;

public class DeckService {

    private final DatabaseCardRepository cardRepository;

    public DeckService(DatabaseCardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }
    public Optional<List<Card>> getDeck(User user) {
        return this.cardRepository.getDeck(user.getId());
    }
}
