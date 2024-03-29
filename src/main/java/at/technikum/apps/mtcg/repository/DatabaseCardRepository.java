package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.apps.mtcg.exceptions.ExceptionMessage;
import at.technikum.apps.mtcg.exceptions.HttpStatusException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseCardRepository {
    private final String FIND_WITH_ID_SQL = "SELECT * FROM cards WHERE id = ?";
    private final String SAVE_SQL = "INSERT INTO cards(id, name, element, type, damage, in_deck, ownerId_fk, packageId_fk) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String GET_CARDS_FROM_PACKAGE_SQL = "SELECT * FROM cards WHERE packageId_fk = ? LIMIT 5";
    private final String GET_CARDS_FROM_USER_SQL = "SELECT * FROM cards WHERE ownerId_fk = ?";
    private final String UPDATE_CARD_OWNER_SQL = "UPDATE cards SET ownerId_fk = ? WHERE id = ?";
    private final String GET_DECK = "SELECT * FROM cards WHERE in_deck = 1 AND ownerId_fk = ?";
    private final String CHECK_FOR_OWNERSHIP = "SELECT * FROM cards WHERE id = ? AND ownerId_fk = ?";
    private final String ADD_CARD_TO_DECK = "UPDATE cards SET in_deck = 1 WHERE id = ?";
    private final String IS_IN_DECK_SQL = "SELECT * FROM cards WHERE id = ? AND in_deck = 1";

    private final Database database = new Database();

    public void save(Card card) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SAVE_SQL)
        ) {
            pstmt.setString(1, card.getId());
            pstmt.setString(2, card.getName());
            pstmt.setString(3, card.getElement());
            pstmt.setString(4, card.getType());
            pstmt.setInt(5, card.getDamage());
            pstmt.setInt(6, card.getInDeck());
            pstmt.setString(7, card.getOwnerId());
            pstmt.setString(8, card.getPackageId());

            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new HttpStatusException(HttpStatus.INTERNAL_ERROR, HttpContentType.TEXT_PLAIN, ExceptionMessage.INTERNAL_SERVER_ERROR);
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
            e.printStackTrace();
            throw new HttpStatusException(HttpStatus.INTERNAL_ERROR, HttpContentType.TEXT_PLAIN, ExceptionMessage.INTERNAL_SERVER_ERROR);
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
            e.printStackTrace();
            throw new HttpStatusException(HttpStatus.INTERNAL_ERROR, HttpContentType.TEXT_PLAIN, ExceptionMessage.INTERNAL_SERVER_ERROR);
        }
        return cards;
    }

    public List<Card> getCardsFromUser(String userId) {
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
            e.printStackTrace();
            throw new HttpStatusException(HttpStatus.INTERNAL_ERROR, HttpContentType.TEXT_PLAIN, ExceptionMessage.INTERNAL_SERVER_ERROR);
        }

        return cards;
    }

    public void updateCardOwner(String cardId, String userId) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(UPDATE_CARD_OWNER_SQL)
        ) {
            pstmt.setString(1, userId);
            pstmt.setString(2, cardId);

            pstmt.execute();
        } catch(SQLException e) {
            e.printStackTrace();
            throw new HttpStatusException(HttpStatus.INTERNAL_ERROR, HttpContentType.TEXT_PLAIN, ExceptionMessage.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Card> getDeck(String userId) {
        List<Card> cards = new ArrayList<>();

        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(GET_DECK)
        ) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    cards.add(mapResultSetToCard(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new HttpStatusException(HttpStatus.INTERNAL_ERROR, HttpContentType.TEXT_PLAIN, ExceptionMessage.INTERNAL_SERVER_ERROR);
        }
        return cards;
    }

    public Optional<Card> checkForOwnership(String cardId, String userId) {
        Optional<Card> card = Optional.empty();
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(CHECK_FOR_OWNERSHIP)
        ) {
            pstmt.setString(1, cardId);
            pstmt.setString(2, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Card foundCard = mapResultSetToCard(rs);
                    card = Optional.of(foundCard);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new HttpStatusException(HttpStatus.INTERNAL_ERROR, HttpContentType.TEXT_PLAIN, ExceptionMessage.INTERNAL_SERVER_ERROR);
        }
        return card;
    }

    public void addCardToDeck(String cardId) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(ADD_CARD_TO_DECK)
        ) {
            pstmt.setString(1, cardId);

            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new HttpStatusException(HttpStatus.INTERNAL_ERROR, HttpContentType.TEXT_PLAIN, ExceptionMessage.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean isInDeck(String cardId) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(IS_IN_DECK_SQL)
        ) {
            pstmt.setString(1, cardId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new HttpStatusException(HttpStatus.INTERNAL_ERROR, HttpContentType.TEXT_PLAIN, ExceptionMessage.INTERNAL_SERVER_ERROR);
        }
    }

    private Card mapResultSetToCard(ResultSet rs) throws SQLException {
        Card card = new Card();
        card.setId(rs.getString("id"));
        card.setName(rs.getString("name"));
        card.setElement(rs.getString("element"));
        card.setType(rs.getString("type"));
        card.setDamage(rs.getInt("damage"));
        card.setInDeck(rs.getInt("in_deck"));
        card.setOwnerId(rs.getString("ownerId_fk"));
        card.setPackageId(rs.getString("packageId_fk"));
        return card;
    }
}
