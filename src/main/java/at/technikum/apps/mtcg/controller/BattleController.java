package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.service.BattleService;
import at.technikum.apps.mtcg.service.DeckService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BattleController extends Controller {

    private final UserService userService;
    private final BattleService battleService;
    private final DeckService deckService;

    public BattleController(JsonParser parser, UserService userService, BattleService battleService, DeckService deckService) {
        super(parser);
        this.userService = userService;
        this.battleService = battleService;
        this.deckService = deckService;
    }

    @Override
    public boolean supports(String route) {
        return route.equals("/battles");
    }

    @Override
    public Response handle(Request request) throws SQLException {
        if (request.getMethod().equals("POST")) {
            return battle(request);
        }
        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getMessage());
    }

    Response battle(Request request) throws SQLException {
        Optional<User> optionalUser = checkForAuthorizedRequest(request, userService);
        if (optionalUser.isEmpty()) {
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED_ACCESS.getMessage());
        }
        User user = optionalUser.get();
        System.out.println(user);
        Optional<List<Card>> retrievedDeck = this.deckService.getDeck(user);
        if (retrievedDeck.isEmpty()) {
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.NO_CONTENT, HttpStatusMessage.NO_CONTENT_DECK.getStatusMessage());
        }
        List<Card> deck = retrievedDeck.get();
        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.OK, this.battleService.createBattleLog(user, userService, deck));
    }
}