package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.service.CardService;
import at.technikum.server.http.*;

public class CardController extends Controller {
    private final CardService cardService;

    public CardController(JsonParser parser, CardService cardService) {
        super(parser);
        this.cardService = cardService;
    }

    @Override
    public boolean supports(String route) {
        return route.equals("/cards");
    }

    @Override
    public Response handle(Request request) {
        if(request.getMethod().equals("GET")) {
            return getAllCards(request);
        }
        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getMessage());
    }

    Response getAllCards(Request request) {
        return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.OK, this.parser.getCards(this.cardService.getAllCardsFromUser(request.getAuthorizationToken())));
    }
}
