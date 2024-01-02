package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseCardRepository;
import at.technikum.server.http.Request;

import java.util.Optional;

public class TransactionService {
    private final DatabaseCardRepository databaseCardRepository;

    public TransactionService(DatabaseCardRepository databaseCardRepository) {
        this.databaseCardRepository = databaseCardRepository;
    }

    public Card[] buyPackage(User user) {
        Card[] cardPackage;
        cardPackage = databaseCardRepository.buyPackage(user);
        return null;
    }
}
