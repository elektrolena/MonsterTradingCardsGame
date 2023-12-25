package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final DatabaseUserRepository userRepository;

    public UserService(DatabaseUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> find(int id) {
        return Optional.empty();
    }

    public User save(User user) {
        user.setId(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

    public User update(int updateId, User updatedUser) {
        return null;
    }

    public User delete(User user) {
        return null;
    }
}
