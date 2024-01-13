package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Battle;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.exceptions.ExceptionMessage;
import at.technikum.apps.mtcg.exceptions.HttpStatusException;
import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.repository.DatabaseBattleRepository;
import at.technikum.server.http.*;

import java.util.List;

public class HistoryService extends Service {
    private final UserService userService;
    private final DatabaseBattleRepository databaseBattleRepository;

    public HistoryService(JsonParser parser, UserService userService, DatabaseBattleRepository databaseBattleRepository) {
        super(parser);
        this.userService = userService;
        this.databaseBattleRepository = databaseBattleRepository;
    }

    public String getBattleHistory(Request request) {
        User user = getLoggedInUser(request, userService);

        List<Battle> battles = this.databaseBattleRepository.getBattleHistory(user.getUsername());
        if(battles.isEmpty()) {
            throw new HttpStatusException(HttpStatus.NO_CONTENT, HttpContentType.TEXT_PLAIN, ExceptionMessage.NO_CONTENT_BATTLE);
        }

        StringBuilder battleHistory = new StringBuilder();
        battleHistory.append("Your Battle History, ").append(user.getName()).append("!\n");
        for(Battle battle : battles) {
            battleHistory.append(parser.getBattle(battle, user));
        }

        return battleHistory.toString();
    }

}
