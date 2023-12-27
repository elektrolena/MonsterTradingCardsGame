package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import at.technikum.apps.mtcg.entity.User;

import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final DatabaseUserRepository userRepository;

    public UserService(DatabaseUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> find(String username) {
        return userRepository.find(username);
    }

    public User save(User user) {
        user.setId(UUID.randomUUID().toString());
        return userRepository.save(user);
    }
}
