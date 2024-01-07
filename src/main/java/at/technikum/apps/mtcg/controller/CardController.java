package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseCardRepository;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import at.technikum.apps.mtcg.service.CardService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.*;

import java.util.List;
import java.util.Optional;

public class CardController extends Controller {

    private final UserService userService;
    private final CardService cardService;

    public CardController() {
        super();
        this.userService = new UserService(new DatabaseUserRepository());
        this.cardService = new CardService(new DatabaseCardRepository());
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
        Optional<User> optionalUser = checkForAuthorizedRequest(request, userService);
        if(optionalUser.isEmpty()){
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED_ACCESS.getMessage());
        }

        User user = optionalUser.get();

        Optional<List<Card>> cards = this.cardService.getAllCardsFromUser(user.getId());
        if(cards.isPresent()) {
            return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.OK, this.parser.getCards(cards.get()));
        } else {
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.NO_CONTENT, HttpStatusMessage.NO_CONTENT_CARD.getStatusMessage());
        }
    }
}
