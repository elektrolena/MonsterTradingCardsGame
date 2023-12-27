package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {

    private final List<User> users;
    private User user;

    public UserRepository() {
        this.users = new ArrayList<>();
    }

    public Optional<User> find(String username) {
        return Optional.ofNullable(user);
    }

    public User save(User user) {
        users.add(user);

        return user;
    }
}
