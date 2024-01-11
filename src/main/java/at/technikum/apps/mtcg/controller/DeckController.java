package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.service.DeckService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DeckController extends Controller {

    private final DeckService deckService;
    private final UserService userService;

    public DeckController(JsonParser parser, DeckService deckService, UserService userService) {
        super(parser);
        this.deckService = deckService;
        this.userService = userService;
    }

    @Override
    public boolean supports(String route) {
        return route.equals("/deck") || route.equals("/deck?format=plain");
    }

    @Override
    public Response handle(Request request) {
        if(request.getMethod().equals("GET")) {
            return getDeck(request);
        } else if(request.getMethod().equals("PUT")) {
            return updateDeck(request);
        }
        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getMessage());
    }

    Response getDeck(Request request) {
        Optional<User> optionalUser = checkForAuthorizedRequest(request, userService);
        if(optionalUser.isEmpty()){
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED_ACCESS.getMessage());
        }

        User user = optionalUser.get();
        Optional<List<Card>> deck = this.deckService.getDeck(user);

        if(deck.isPresent()) {
            if(request.getRoute().equals("/deck")) {
                return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.OK, this.parser.getCards(deck.get()));
            } else {
                return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.OK, this.parser.getCardsPlain(deck.get()));
            }

        } else {
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.NO_CONTENT, HttpStatusMessage.NO_CONTENT_DECK.getStatusMessage());
        }
    }

    Response updateDeck(Request request) {
        Optional<User> optionalUser = checkForAuthorizedRequest(request, userService);
        if(optionalUser.isEmpty()){
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED_ACCESS.getMessage());
        }

        User user = optionalUser.get();

        switch(this.deckService.updateDeck(user, this.parser.getCardsFromBody(request))) {
            case 200:
                return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.OK, HttpStatusMessage.OK_DECK.getStatusMessage());
            case 400:
                return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.BAD_REQUEST, HttpStatusMessage.BAD_REQUEST_DECK.getStatusMessage());
            case 403:
                return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.FORBIDDEN, HttpStatusMessage.FORBIDDEN_DECK.getStatusMessage());
            default:
                return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getMessage());
        }
    }
}
