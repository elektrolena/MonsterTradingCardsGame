package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseUserRepository {

    private final String FIND_ALL_SQL = "SELECT * FROM users";
    private final String FIND_SQL = "SELECT * FROM users WHERE username = ?";

    private final String SAVE_SQL = "INSERT INTO users(id, username, password) VALUES(?, ?, ?)";

    private final Database database = new Database();

    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_ALL_SQL);
                ResultSet rs = pstmt.executeQuery()
        ) {
            while (rs.next()) {
                User user = new User(
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                users.add(user);
            }

            return users;
        } catch (SQLException e) {
            return users;
        }
    }

    public Optional<User> find(String username) {
        Optional<User> user = Optional.empty();

        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_SQL);
        ) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User foundUser = mapResultSetToUser(rs);
                    user = Optional.of(foundUser);
                }
            }
        } catch (SQLException e) {
            return user;
        }

        return user;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId((rs.getString("id")));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        return user;
    }

    public User save(User user) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SAVE_SQL)
        ) {
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());

            pstmt.execute();
        } catch (SQLException e) {
            // THOUGHT: how do I handle exceptions (hint: look at the TaskApp)

        }

        return user;
    }
}
