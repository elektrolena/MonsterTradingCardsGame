package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseCardRepository;

public class TransactionService {
    private final DatabaseCardRepository databaseCardRepository;

    public TransactionService(DatabaseCardRepository databaseCardRepository) {
        this.databaseCardRepository = databaseCardRepository;
    }

    public Card[] buyPackage(User user, UserService userService) {
        Card[] cardPackage;
        cardPackage = databaseCardRepository.buyPackage(user);
        if(cardPackage.length == 5) {
            userService.updateCoins(user.getId(), user.getCoins() - 5);
        }
        return null;
    }
}
