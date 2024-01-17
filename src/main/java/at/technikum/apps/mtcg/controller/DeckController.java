package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.exceptions.ExceptionMessage;
import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.service.DeckService;
import at.technikum.server.http.*;

import java.util.List;

public class DeckController extends Controller {

    private final DeckService deckService;

    public DeckController(JsonParser parser, DeckService deckService) {
        super(parser);
        this.deckService = deckService;
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
        List<Card> deck = this.deckService.getDeck(request.getAuthorizationToken());

        if(request.getRoute().equals("/deck")) {
            return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.OK, this.parser.getCards(deck));
        } else {
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.OK, this.parser.getCardsPlain(deck));
        }
    }

    Response updateDeck(Request request) {
        this.deckService.updateDeck(request.getAuthorizationToken(), this.parser.getCardsFromBody(request));

        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.OK, ExceptionMessage.OK_DECK.getStatusMessage());
    }
}
