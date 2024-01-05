package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseCardRepository;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;

import java.util.ArrayList;
import java.util.List;

public class BattleService {
    private final CardService cardService;
    private final List<User> queue;

    public BattleService() {
        this.cardService = new CardService(new DatabaseCardRepository());
        this.queue = new ArrayList<>();
    }

    public String createBattleLog(User user, UserService userService) {
        return "The battle has been carried out successfully.";
    }

}
