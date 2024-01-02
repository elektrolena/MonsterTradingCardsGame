package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Package;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseCardRepository {
    private final String FIND_WITH_ID_SQL = "SELECT * FROM cards WHERE id = ?";
    private final String SAVE_SQL = "INSERT INTO cards(id, name, element, damage, ownerId_fk, packageId_fk) VALUES (?, ?, ?, ?, ?, ?)";
    private final String GET_CARDS_FROM_PACKAGE_SQL = "SELECT * FROM cards WHERE packageId_fk = ? LIMIT 5";
    private final String GET_CARDS_FROM_USER_SQL = "SELECT * FROM cards WHERE ownerId_fk = ?";
    private final String UPDATE_OWNER_SQL = "UPDATE cards SET ownerId_fk = ? WHERE id = ?";

    private final Database database = new Database();


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

    public List<Card> getCardsFromPackage(String packageId) {
        List<Card> cards = new ArrayList<>();

        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(GET_CARDS_FROM_PACKAGE_SQL)
        ) {
            pstmt.setString(1, packageId);

            try (ResultSet rs = pstmt.executeQuery()) {
                int index = 0;
                while (rs.next() && index < 5) {
                    cards.add(mapResultSetToCard(rs));
                    index++;
                }
            }
        } catch (SQLException e) {

        }
        return cards;
    }

    public Optional<List<Card>> getCardsFromUser(String userId) {
        List<Card> cards = new ArrayList<>();

        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(GET_CARDS_FROM_USER_SQL)
        ) {
            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    cards.add(mapResultSetToCard(rs));
                }
            }
        } catch (SQLException e) {
            // Handle SQLException
        }

        return cards.isEmpty() ? Optional.empty() : Optional.of(cards);
    }


    public void updateCardsOwner(List<Card> cards, String userId) {
        try (Connection con = database.getConnection()) {
            for (Card card : cards) {
                try (PreparedStatement pstmt = con.prepareStatement(UPDATE_OWNER_SQL)) {
                    pstmt.setString(1, userId);
                    pstmt.setString(2, card.getId());

                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            // Handle SQLException
        }
    }

    private Card mapResultSetToCard(ResultSet rs) throws SQLException {
        Card card = new Card();
        card.setId((rs.getString("id")));
        card.setName((rs.getString("name")));
        card.setElement(rs.getString("element"));
        card.setDamage(rs.getInt("damage"));
        card.setOwnerId(rs.getString("ownerId_fk"));
        card.setPackageId(rs.getString("packageId_fk"));
        return card;
    }
}
