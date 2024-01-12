package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.Battle;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.HttpStatusException;
import at.technikum.server.http.HttpStatusMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseBattleRepository {
    private final String SAVE_BATTLE_SQL = "INSERT INTO battles(id, winnerId_fk, loserId_fk, log, is_draw) VALUES (?, ?, ?, ?, ?)";

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
            throw new HttpStatusException(HttpStatus.INTERNAL_ERROR, HttpContentType.TEXT_PLAIN, HttpStatusMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
