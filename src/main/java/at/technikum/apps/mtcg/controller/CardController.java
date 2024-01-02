package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.util.Optional;

public class CardController extends Controller {

    private final UserService userService;

    public CardController() {
        this.userService = new UserService(new DatabaseUserRepository());
    }

    @Override
    public boolean supports(String route) {
        return route.equals("/cards");
    }

    @Override
    public Response handle(Request request) {
        if(request.getMethod() == "GET") {
            return getAllCards(request);
        }
        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getMessage());
    }

    private Response getAllCards(Request request) {
        Optional<User> optionalUser = checkForAuthorizedRequest(request, userService);
        if(optionalUser.isEmpty()){
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED_ACCESS.getMessage());
        }

        User admin = optionalUser.get();
    }
}
