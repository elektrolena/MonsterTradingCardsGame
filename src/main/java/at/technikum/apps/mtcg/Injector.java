package at.technikum.apps.mtcg;

import at.technikum.apps.mtcg.controller.*;
import at.technikum.apps.mtcg.dto.BattleLog;
import at.technikum.apps.mtcg.entity.Battle;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.game.BattleLogic;
import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.repository.*;
import at.technikum.apps.mtcg.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Injector {

    /*
    A central place to create all classes.
    Dependency Injection via constructor injection.
     */
    public List<Controller> createController() {

        List<Controller> controllerList = new ArrayList<>();

        // JsonParser
        JsonParser jsonParser = new JsonParser();

        // repositories
        DatabaseUserRepository databaseUserRepository = new DatabaseUserRepository();
        DatabaseCardRepository databaseCardRepository = new DatabaseCardRepository();
        DatabasePackageRepository databasePackageRepository = new DatabasePackageRepository();
        DatabaseTradingRepository databaseTradingRepository = new DatabaseTradingRepository();
        DatabaseBattleRepository databaseBattleRepository = new DatabaseBattleRepository();

        // battle
        Battle battle = new Battle();
        BattleLogic battleLogic = new BattleLogic(databaseUserRepository, databaseBattleRepository);
        ConcurrentHashMap<User, List<Card>> queue = new ConcurrentHashMap<>();

        // services
        UserService userService = new UserService(databaseUserRepository);
        AuthorizationService authorizationService = new AuthorizationService(userService);
        userService.setAuthorizationService(authorizationService);
        SessionService sessionService = new SessionService(databaseUserRepository);
        PackageService packageService = new PackageService(authorizationService, databaseCardRepository, databasePackageRepository);
        TransactionService transactionService = new TransactionService(authorizationService, databaseCardRepository, databasePackageRepository);
        CardService cardService = new CardService(authorizationService, databaseCardRepository);
        DeckService deckService = new DeckService(authorizationService, databaseCardRepository);
        StatsService statsService = new StatsService(authorizationService, databaseUserRepository);
        TradingService tradingService = new TradingService(authorizationService, databaseCardRepository, databaseTradingRepository);
        BattleService battleService = new BattleService(authorizationService, deckService, battle, battleLogic, queue);
        HistoryService historyService = new HistoryService(authorizationService, databaseBattleRepository);

        // controllers
        controllerList.add(new UserController(jsonParser, userService));
        controllerList.add(new SessionController(jsonParser, sessionService));
        controllerList.add(new PackageController(jsonParser, packageService));
        controllerList.add(new TransactionController(jsonParser, userService, transactionService));
        controllerList.add(new CardController(jsonParser, userService, cardService));
        controllerList.add(new DeckController(jsonParser, deckService));
        controllerList.add(new StatsController(jsonParser, userService, statsService));
        controllerList.add(new BattleController(jsonParser, userService, battleService, deckService));
        controllerList.add(new TradingController(jsonParser, tradingService));
        controllerList.add(new HistoryController(jsonParser, historyService));

        return controllerList;
    }
}