package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.exceptions.ExceptionMessage;
import at.technikum.apps.mtcg.exceptions.HttpStatusException;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final DatabaseUserRepository databaseUserRepository;

    public UserService(DatabaseUserRepository databaseUserRepository) {
        this.databaseUserRepository = databaseUserRepository;
    }

    public User findWithUsername(String username) {
        Optional<User> user = databaseUserRepository.findWithUsername(username);
        if(user.isEmpty()) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, HttpContentType.TEXT_PLAIN, ExceptionMessage.NOT_FOUND_USER);
        }
        return user.get();
    }

    public Optional<User> findWithToken(String token) {
        return databaseUserRepository.findWithToken(token);
    }

    public User update(User user, User updatedUser) {
        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }
        if (updatedUser.getPassword() != null) {
            user.setPassword(updatedUser.getPassword());
        }
        if (updatedUser.getBio() != null) {
            user.setBio(updatedUser.getBio());
        }
        if (updatedUser.getImage() != null) {
            user.setImage(updatedUser.getImage());
        }

        return databaseUserRepository.update(user);
    }

    public void updateCoins(String userId, int sum) {
        this.databaseUserRepository.updateCoins(userId, sum);
    }

    public User save(User user) {
        if(this.databaseUserRepository.findWithUsername(user.getUsername()).isPresent()) {
            throw new HttpStatusException(HttpStatus.ALREADY_EXISTS, HttpContentType.TEXT_PLAIN, ExceptionMessage.ALREADY_EXISTS_USER);
        }
        user.setId(UUID.randomUUID().toString());
        user.setCoins(20);
        user.setElo(100);
        user.setWins(0);
        user.setLosses(0);
        return databaseUserRepository.save(user);
    }

    public List<User> getUserScoreBoard() {
        return databaseUserRepository.getUserScoreBoard();
    }
}
