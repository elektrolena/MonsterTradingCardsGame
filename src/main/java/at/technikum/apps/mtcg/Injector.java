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

        // repositories
        DatabaseUserRepository databaseUserRepository = new DatabaseUserRepository();
        DatabaseCardRepository databaseCardRepository = new DatabaseCardRepository();
        DatabasePackageRepository databasePackageRepository = new DatabasePackageRepository();
        DatabaseTradingRepository databaseTradingRepository = new DatabaseTradingRepository();
        DatabaseBattleRepository databaseBattleRepository = new DatabaseBattleRepository();

        // battle
        Battle battle = new Battle();
        BattleLogic battleLogic = new BattleLogic(databaseBattleRepository);
        ConcurrentHashMap<User, List<Card>> queue = new ConcurrentHashMap<>();

        // services
        UserService userService = new UserService(databaseUserRepository);
        SessionService sessionService = new SessionService(databaseUserRepository);
        PackageService packageService = new PackageService(databaseCardRepository, databasePackageRepository);
        TransactionService transactionService = new TransactionService(databaseCardRepository, databasePackageRepository);
        CardService cardService = new CardService(databaseCardRepository);
        DeckService deckService = new DeckService(databaseCardRepository);
        TradingService tradingService = new TradingService(databaseCardRepository, databaseTradingRepository);
        BattleService battleService = new BattleService(battle, battleLogic, cardService, queue);

        // JsonParser
        JsonParser jsonParser = new JsonParser();

        // TODO: ask if there is a better place for this: app startup logic
        // delete all Session Tokens
        databaseUserRepository.deleteTokens();

        // controllers
        controllerList.add(new UserController(jsonParser, userService));
        controllerList.add(new SessionController(jsonParser, sessionService));
        controllerList.add(new PackageController(jsonParser, userService, packageService));
        controllerList.add(new TransactionController(jsonParser, userService, transactionService));
        controllerList.add(new CardController(jsonParser, userService, cardService));
        controllerList.add(new DeckController(jsonParser, deckService, userService));
        controllerList.add(new StatsController(jsonParser, userService));
        controllerList.add(new BattleController(jsonParser, userService, battleService, deckService));
        controllerList.add(new TradingController(jsonParser, userService, tradingService));

        return controllerList;
    }
}