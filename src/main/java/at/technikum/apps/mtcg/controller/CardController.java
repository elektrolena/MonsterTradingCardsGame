package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.service.CardService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.*;

public class CardController extends Controller {

    private final UserService userService;
    private final CardService cardService;

    public CardController(JsonParser parser, UserService userService, CardService cardService) {
        super(parser);
        this.userService = userService;
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
