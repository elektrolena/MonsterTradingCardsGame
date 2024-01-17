package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;

import java.util.List;

public class StatsService {
    private final AuthorizationService authorizationService;
    private final DatabaseUserRepository databaseUserRepository;

    public StatsService(AuthorizationService authorizationService, DatabaseUserRepository databaseUserRepository) {
        this.authorizationService = authorizationService;
        this.databaseUserRepository = databaseUserRepository;
    }
    public List<User> getUserScoreBoard(String authorizationToken) {
        this.authorizationService.getLoggedInUser(authorizationToken);

        return databaseUserRepository.getUserScoreBoard();
    }
}
