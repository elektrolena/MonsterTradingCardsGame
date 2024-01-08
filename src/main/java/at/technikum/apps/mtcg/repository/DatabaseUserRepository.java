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
    private final String FIND_WITH_USERNAME_SQL = "SELECT * FROM users WHERE username = ?";
    private final String FIND_WITH_TOKEN_SQL = "SELECT * FROM users WHERE token = ?";
    private final String SAVE_SQL = "INSERT INTO users(id, username, password, bio, image, coins, elo, wins, losses) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE users SET username = ?, password = ?, bio = ?, image = ? WHERE id = ?";
    private final String UPDATE_COINS_SQL = "UPDATE users SET coins = ? WHERE id = ?";
    private final String VALIDATE_LOGIN_SQL = "SELECT * FROM users WHERE username = ? AND password = ?";
    private final String ADD_TOKEN_SQL = "UPDATE users SET token = ? WHERE username = ? AND password = ?";
    private final String DELETE_ALL_TOKENS_SQL = "UPDATE users SET token = NULL";
    private final String GET_USER_SCOREBOARD_SQL = "SELECT * FROM users ORDER BY elo DESC";

    private final Database database = new Database();

    public User save(User user) throws SQLException {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SAVE_SQL)
        ) {
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getBio());
            pstmt.setString(5, user.getImage());
            pstmt.setInt(6, user.getCoins());
            pstmt.setInt(7, user.getElo());
            pstmt.setInt(8, user.getWins());
            pstmt.setInt(9, user.getLosses());

            pstmt.execute();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return user;
    }

    public Optional<User> findWithUsername(String username) throws SQLException {
        Optional<User> user = Optional.empty();

        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_WITH_USERNAME_SQL)
        ) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User foundUser = mapResultSetToUser(rs);
                    user = Optional.of(foundUser);
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return user;
    }

    public Optional<User> findWithToken(String token) throws SQLException {
        Optional<User> user = Optional.empty();

        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_WITH_TOKEN_SQL)
        ) {
            pstmt.setString(1, token);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User foundUser = mapResultSetToUser(rs);
                    user = Optional.of(foundUser);
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return user;
    }

    public User update(User user) throws SQLException {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(UPDATE_SQL)
        ) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getBio());
            pstmt.setString(4, user.getImage());
            pstmt.setString(5, user.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }

        return user;
    }

    public void updateCoins(String userId, int sum) throws SQLException {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(UPDATE_COINS_SQL)
        ) {
            pstmt.setInt(1, sum);
            pstmt.setString(2, userId);

            pstmt.execute();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public Optional<User> validateLogin(User user) throws SQLException {
        Optional <User> userToReturn = Optional.empty();

        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(VALIDATE_LOGIN_SQL)
        ) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User foundUser = mapResultSetToUser(rs);
                    userToReturn = Optional.of(foundUser);
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }

        return userToReturn;
    }

    public void addToken(User user) throws SQLException {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(ADD_TOKEN_SQL)
        ) {
            pstmt.setString(1, user.getToken());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());

            pstmt.execute();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public void deleteTokens() {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(DELETE_ALL_TOKENS_SQL)
        ) {
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getUserScoreBoard() throws SQLException {
        List<User> users = new ArrayList<>();

        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(GET_USER_SCOREBOARD_SQL)
        ) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }

        return users;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setToken(rs.getString("token"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setBio(rs.getString("bio"));
        user.setImage(rs.getString("image"));
        user.setCoins(rs.getInt("coins"));
        user.setElo(rs.getInt("elo"));
        user.setWins(rs.getInt("wins"));
        user.setLosses(rs.getInt("losses"));
        return user;
    }
}
