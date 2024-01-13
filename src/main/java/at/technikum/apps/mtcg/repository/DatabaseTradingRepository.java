package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.TradingDeal;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.apps.mtcg.exceptions.HttpStatusException;
import at.technikum.apps.mtcg.exceptions.ExceptionMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseTradingRepository {
    private final String GET_ALL_TRADINGDEALS_SQL = "SELECT * FROM tradings";
    private final String GET_TRADINGDEAL_SQL = "SELECT * FROM tradings WHERE id = ?";
    private final String DOES_TRADING_DEAL_EXIST_SQL = "SELECT * FROM tradings WHERE id = ?";
    private final String SAVE_TRADING_DEAL_SQL = "INSERT INTO tradings(id, userId_fk, cardId_fk, desiredType, desiredDamage) VALUES (?, ?, ?, ?, ?)";
    private final String DELETE_TRADING_DEAL_SQL = "DELETE FROM tradings WHERE id = ?";
    private final Database database = new Database();

    public Optional<List<TradingDeal>> getAllTradingDeals() {
        List<TradingDeal> tradingDeals = new ArrayList<>();

        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(GET_ALL_TRADINGDEALS_SQL)
        ) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tradingDeals.add(mapResultSetToTradingDeal(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new HttpStatusException(HttpStatus.INTERNAL_ERROR, HttpContentType.TEXT_PLAIN, ExceptionMessage.INTERNAL_SERVER_ERROR);
        }

        return tradingDeals.isEmpty() ? Optional.empty() : Optional.of(tradingDeals);
    }

    public Optional<TradingDeal> getTradingDeal(String tradingDealId) {
        Optional<TradingDeal> tradingDeal = Optional.empty();

        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(GET_TRADINGDEAL_SQL)
        ) {
            pstmt.setString(1, tradingDealId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    TradingDeal foundDeal= mapResultSetToTradingDeal(rs);
                    tradingDeal = Optional.of(foundDeal);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new HttpStatusException(HttpStatus.INTERNAL_ERROR, HttpContentType.TEXT_PLAIN, ExceptionMessage.INTERNAL_SERVER_ERROR);
        }
        return tradingDeal;
    }

    public boolean doesTradingDealExist(String tradingDealId) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(DOES_TRADING_DEAL_EXIST_SQL)
        ) {
            pstmt.setString(1, tradingDealId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new HttpStatusException(HttpStatus.INTERNAL_ERROR, HttpContentType.TEXT_PLAIN, ExceptionMessage.INTERNAL_SERVER_ERROR);
        }
    }

    public void saveTradingDeal(TradingDeal tradingDeal) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SAVE_TRADING_DEAL_SQL)
        ) {
            pstmt.setString(1, tradingDeal.getId());
            pstmt.setString(2, tradingDeal.getUserId());
            pstmt.setString(3, tradingDeal.getCardId());
            pstmt.setString(4, tradingDeal.getDesiredType());
            pstmt.setInt(5, tradingDeal.getDesiredDamage());

            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new HttpStatusException(HttpStatus.INTERNAL_ERROR, HttpContentType.TEXT_PLAIN, ExceptionMessage.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteTradingDeal(String tradingDealId) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(DELETE_TRADING_DEAL_SQL)
        ) {
            pstmt.setString(1, tradingDealId);

            pstmt.execute();
        } catch(SQLException e) {
            e.printStackTrace();
            throw new HttpStatusException(HttpStatus.INTERNAL_ERROR, HttpContentType.TEXT_PLAIN, ExceptionMessage.INTERNAL_SERVER_ERROR);
        }
    }

    private TradingDeal mapResultSetToTradingDeal(ResultSet rs) throws SQLException {
        TradingDeal tradingDeal = new TradingDeal();
        tradingDeal.setId(rs.getString("id"));
        tradingDeal.setUserId(rs.getString("userId_fk"));
        tradingDeal.setCardId(rs.getString("cardId_fk"));
        tradingDeal.setDesiredType(rs.getString("desiredType"));
        tradingDeal.setDesiredDamage(rs.getInt("desiredDamage"));
        return tradingDeal;
    }
}
