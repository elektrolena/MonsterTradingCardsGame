package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseCardRepository;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import at.technikum.apps.mtcg.service.DeckService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.util.List;
import java.util.Optional;

public class DeckController extends Controller {

    private final DeckService deckService;
    private final UserService userService;

    public DeckController() {
        this.deckService = new DeckService(new DatabaseCardRepository());
        this.userService = new UserService(new DatabaseUserRepository());
    }

    @Override
    public boolean supports(String route) {
        return route.equals("/deck");
    }

    @Override
    public Response handle(Request request) {
        if(request.getMethod().equals("GET")) {
            return getDeck(request);
        } else if(request.getMethod().equals("PUT")) {
            return null;
        }
        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getMessage());
    }

    private Response getDeck(Request request) {
        Optional<User> optionalUser = checkForAuthorizedRequest(request, userService);
        if(optionalUser.isEmpty()){
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED_ACCESS.getMessage());
        }

        User user = optionalUser.get();
        Optional<List<Card>> deck = this.deckService.getDeck(user);

        if(deck.isPresent()) {
            return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.OK, convertObjectListToJson(deck.get()));
        } else {
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.NOT_FOUND, "The request was fine, but the deck doesn't have any cards.");
        }
    }
}
