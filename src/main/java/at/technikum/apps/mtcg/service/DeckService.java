package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseDeckRepository;

import java.util.List;
import java.util.Optional;

public class DeckService {

    private final DatabaseDeckRepository deckRepository;

    public DeckService(DatabaseDeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }
    public Optional<List<Card>> getDeck(User user) {
        return Optional.empty();
    }
}
