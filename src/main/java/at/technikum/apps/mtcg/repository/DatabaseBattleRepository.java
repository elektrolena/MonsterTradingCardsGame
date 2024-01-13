package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.Battle;
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

public class DatabaseBattleRepository {
    private final String SAVE_BATTLE_SQL = "INSERT INTO battles(id, winnerUsername_fk, loserUsername_fk, log, is_draw) VALUES (?, ?, ?, ?, ?)";
    private final String GET_BATTLE_HISTORY_SQL = "SELECT * FROM battles WHERE winnerUsername_fk = ? OR loserUsername_fk = ?";

    private final Database database = new Database();

    public void save(Battle battle) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SAVE_BATTLE_SQL)
        ) {
            pstmt.setString(1, battle.getId());
            pstmt.setString(2, battle.getWinner());
            pstmt.setString(3, battle.getLoser());
            pstmt.setString(4, battle.getLog());
            pstmt.setBoolean(5, battle.isDraw());

            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new HttpStatusException(HttpStatus.INTERNAL_ERROR, HttpContentType.TEXT_PLAIN, ExceptionMessage.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Battle> getBattleHistory(String username) {
        List<Battle> battles = new ArrayList<>();
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(GET_BATTLE_HISTORY_SQL)
        ) {
            pstmt.setString(1, username);
            pstmt.setString(2, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    battles.add(mapResultSetToBattle(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new HttpStatusException(HttpStatus.INTERNAL_ERROR, HttpContentType.TEXT_PLAIN, ExceptionMessage.INTERNAL_SERVER_ERROR);
        }
        return battles;
    }

    private Battle mapResultSetToBattle(ResultSet rs) throws SQLException {
        Battle battle = new Battle();
        battle.setId(rs.getString("id"));
        battle.setWinner(rs.getString("winnerUsername_fk"));
        battle.setLoser(rs.getString("loserUsername_fk"));
        battle.setLog(rs.getString("log"));
        battle.setDraw(rs.getBoolean("is_draw"));
        return battle;
    }
}
