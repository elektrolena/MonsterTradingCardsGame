package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Battle;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.exceptions.ExceptionMessage;
import at.technikum.apps.mtcg.exceptions.HttpStatusException;
import at.technikum.apps.mtcg.repository.DatabaseBattleRepository;
import at.technikum.server.http.*;

import java.util.List;

public class HistoryService {
    private final AuthorizationService authorizationService;
    private final DatabaseBattleRepository databaseBattleRepository;

    public HistoryService(AuthorizationService authorizationService, DatabaseBattleRepository databaseBattleRepository) {
        this.authorizationService = authorizationService;
        this.databaseBattleRepository = databaseBattleRepository;
    }

    public String getBattleHistory(String authorizationToken) {
        User user = this.authorizationService.getLoggedInUser(authorizationToken);

        List<Battle> battles = this.databaseBattleRepository.getBattleHistory(user.getUsername());
        if(battles.isEmpty()) {
            throw new HttpStatusException(HttpStatus.NO_CONTENT, HttpContentType.TEXT_PLAIN, ExceptionMessage.NO_CONTENT_BATTLE);
        }

        StringBuilder battleHistory = new StringBuilder();
        battleHistory.append("Your Battle History, ").append(user.getName()).append("!\n");
        for(Battle battle : battles) {
            battleHistory.append(getBattle(battle, user));
        }

        return battleHistory.toString();
    }

    public String getBattle(Battle battle, User user) {
        return createBattleHistoryString(battle, user);
    }

    private String createBattleHistoryString(Battle battle, User user) {
        StringBuilder battleString = new StringBuilder();
        if(battle.isDraw()) {
            battleString.append("+++ DRAW +++\n");
            if(battle.getWinner().equals(user.getUsername())) {
                battleString.append("  - Opponent: ").append(battle.getLoser());
            } else {
                battleString.append("  - Opponent: ").append(battle.getWinner());
            }
        } else if(battle.getWinner().equals(user.getUsername())) {
            battleString.append("*** VICTORY ***\n");
            battleString.append("  - Opponent: ").append(battle.getLoser());
        } else {
            battleString.append("--- DEFEAT ---\n");
            battleString.append("  - Opponent: ").append(battle.getWinner());
        }
        battleString.append("\n\n");
        return battleString.toString();
    }
}
