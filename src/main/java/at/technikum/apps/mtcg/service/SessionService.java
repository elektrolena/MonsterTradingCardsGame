package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.exceptions.ExceptionMessage;
import at.technikum.apps.mtcg.exceptions.HttpStatusException;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;

import java.sql.Timestamp;
import java.util.Optional;

public class SessionService {

    private final DatabaseUserRepository databaseUserRepository;

    public SessionService(DatabaseUserRepository databaseUserRepository) {
        this.databaseUserRepository = databaseUserRepository;
    }

    public User login(User user) {
        Optional<User> foundUser = databaseUserRepository.validateLogin(user);

        if(foundUser.isEmpty()) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED_ACCESS, HttpContentType.TEXT_PLAIN, ExceptionMessage.UNAUTHORIZED_SESSION);
        }
        user = foundUser.get();

        user.setToken(user.getUsername() + "-mtcgToken");
        user.setLoginTimestamp(new Timestamp(System.currentTimeMillis()));

        databaseUserRepository.addToken(user);

        return user;
    }
}
