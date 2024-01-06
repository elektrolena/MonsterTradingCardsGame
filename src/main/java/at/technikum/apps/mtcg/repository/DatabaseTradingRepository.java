package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.TradingDeal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseTradingRepository {
    private final String GET_ALL_TRADINGDEALS_SQL = "SELECT * FROM tradings";
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
            // Handle SQLException
        }

        return tradingDeals.isEmpty() ? Optional.empty() : Optional.of(tradingDeals);
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
