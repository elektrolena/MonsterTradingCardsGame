package at.technikum.apps.mtcg;

import at.technikum.apps.mtcg.controller.*;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.game.Battle;
import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.repository.DatabaseCardRepository;
import at.technikum.apps.mtcg.repository.DatabasePackageRepository;
import at.technikum.apps.mtcg.repository.DatabaseTradingRepository;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import at.technikum.apps.mtcg.service.*;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.Pack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Injector {

    /*
    A central place to create all classes.
    Dependency Injection via constructor injection.
     */
    public List<Controller> createController() {
        List<Controller> controllerList = new ArrayList<>();

        // battle
        Battle battle = new Battle();
        ConcurrentHashMap<User, List<Card>> queue = new ConcurrentHashMap<>();
        ReentrantLock queueLock = new ReentrantLock();

        // repositories
        DatabaseUserRepository databaseUserRepository = new DatabaseUserRepository();
        DatabaseCardRepository databaseCardRepository = new DatabaseCardRepository();
        DatabasePackageRepository databasePackageRepository = new DatabasePackageRepository();
        DatabaseTradingRepository databaseTradingRepository = new DatabaseTradingRepository();

        // services
        UserService userService = new UserService(databaseUserRepository);
        SessionService sessionService = new SessionService(databaseUserRepository);
        PackageService packageService = new PackageService(databaseCardRepository, databasePackageRepository);
        TransactionService transactionService = new TransactionService(databaseCardRepository, databasePackageRepository);
        CardService cardService = new CardService(databaseCardRepository);
        DeckService deckService = new DeckService(databaseCardRepository);
        TradingService tradingService = new TradingService(databaseCardRepository, databaseTradingRepository);
        BattleService battleService = new BattleService(battle, cardService, queue, queueLock);

        // JsonParser
        JsonParser jsonParser = new JsonParser();

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