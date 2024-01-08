package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.repository.DatabaseCardRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CardService {
    private final DatabaseCardRepository databaseCardRepository;

    public CardService(DatabaseCardRepository databaseCardRepository) {
        this.databaseCardRepository = databaseCardRepository;
    }

    public Optional<List<Card>> getAllCardsFromUser(String userId) throws SQLException {
        return this.databaseCardRepository.getCardsFromUser(userId);
    }
}
