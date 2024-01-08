package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import at.technikum.apps.mtcg.entity.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final DatabaseUserRepository databaseUserRepository;

    public UserService(DatabaseUserRepository databaseUserRepository) {
        this.databaseUserRepository = databaseUserRepository;
    }

    public Optional<User> findWithUsername(String username) throws SQLException {
        return databaseUserRepository.findWithUsername(username);
    }

    public Optional<User> findWithToken(String token) throws SQLException {
        return databaseUserRepository.findWithToken(token);
    }

    public User update(User user, User updatedUser) throws SQLException {
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

    public void updateCoins(String userId, int sum) throws SQLException {
        this.databaseUserRepository.updateCoins(userId, sum);
    }

    public User save(User user) throws SQLException {
        user.setId(UUID.randomUUID().toString());
        user.setCoins(20);
        user.setElo(100);
        user.setWins(0);
        user.setLosses(0);
        return databaseUserRepository.save(user);
    }

    public List<User> getUserScoreBoard() throws SQLException {
        return databaseUserRepository.getUserScoreBoard();
    }
}
