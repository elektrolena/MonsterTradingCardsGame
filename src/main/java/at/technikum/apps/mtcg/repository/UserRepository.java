package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {

    private final List<User> users;

    public UserRepository() {
        this.users = new ArrayList<>();
    }
    public List<User> findAll() {
        return users;
    }

    public Optional<User> find(int id) {
        return Optional.empty();
    }

    public User save(User user) {
        users.add(user);

        return user;
    }

    public User delete(User user) {
        // TODO
        return user;
    }
}
