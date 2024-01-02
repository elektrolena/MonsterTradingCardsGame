package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.entity.Package;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class DatabaseCardRepository {
    private final String FIND_WITH_ID_SQL = "SELECT * FROM cards WHERE id = ?";
    private final String SAVE_SQL = "INSERT INTO cards(id, name, element, damage, ownerId_fk, packageId_fk) VALUES (?, ?, ?, ?, ?, ?)";
    private final String SAVE_PACKAGE_SQL = "INSERT INTO packages(id, price) VALUES (?, ?)";

    private final Database database = new Database();

    public Optional<Card> findWithId(String id) {
        Optional<Card> card = Optional.empty();

        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_WITH_ID_SQL)
        ) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Card foundCard = mapResultSetToCard(rs);
                    card = Optional.of(foundCard);
                }
            }
        } catch (SQLException e) {

        }
        return card;
    }

    public void save(Card card) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SAVE_SQL)
        ) {
            pstmt.setString(1, card.getId());
            pstmt.setString(2, card.getName());
            pstmt.setString(3, card.getElement());
            pstmt.setInt(4, card.getDamage());
            pstmt.setString(5, card.getOwnerId());
            pstmt.setString(6, card.getPackageId());

            pstmt.execute();
        } catch (SQLException e) {

        }
    }

    public void savePackage(Package cardPackage) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SAVE_PACKAGE_SQL)
        ) {
            pstmt.setString(1, cardPackage.getId());
            pstmt.setInt(2, cardPackage.getPrice());

            pstmt.execute();
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public Card[] buyPackage(User user) {
        return null;
    }

    private Card mapResultSetToCard(ResultSet rs) throws SQLException {
        Card card = new Card();
        card.setId((rs.getString("id")));
        card.setName((rs.getString("name")));
        card.setElement(rs.getString("element"));
        card.setDamage(rs.getInt("damage"));
        card.setOwnerId(rs.getString("ownerId"));
        card.setPackageId(rs.getString("packageId"));
        return card;
    }
}
