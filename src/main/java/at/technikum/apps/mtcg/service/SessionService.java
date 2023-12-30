package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;

import java.util.Optional;

public class SessionService {

    private final DatabaseUserRepository databaseUserRepository;

    public SessionService(DatabaseUserRepository databaseUserRepository) {
        this.databaseUserRepository = databaseUserRepository;
    }

    public Optional<User> login(User user) {
        Optional<User> foundUser = databaseUserRepository.validateLogin(user);

        if(foundUser.isPresent()) {
            user = foundUser.get();
            user.setToken(user.getUsername() + "-mtcgToken");
            databaseUserRepository.addToken(user);
            foundUser = Optional.of(user);
        }
        return foundUser;
    }
}
