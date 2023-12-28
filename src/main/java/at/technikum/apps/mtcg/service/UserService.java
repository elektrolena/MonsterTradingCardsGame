package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import at.technikum.apps.mtcg.entity.User;

import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final DatabaseUserRepository databaseUserRepository;

    public UserService(DatabaseUserRepository databaseUserRepository) {
        this.databaseUserRepository = databaseUserRepository;
    }

    public Optional<User> find(String username) {
        return databaseUserRepository.find(username);
    }

    public User update(User user, User updatedUser) {
        if (updatedUser.getUsername() != null) {
            user.setUsername(updatedUser.getUsername());
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

    public User save(User user) {
        user.setId(UUID.randomUUID().toString());
        return databaseUserRepository.save(user);
    }
}
