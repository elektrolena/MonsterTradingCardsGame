package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import at.technikum.apps.mtcg.service.SessionService;
import at.technikum.server.http.*;

import java.util.Optional;

public class SessionController extends Controller {

    private final SessionService sessionService;

    public SessionController() {
        super();
        this.sessionService = new SessionService(new DatabaseUserRepository());
    }

    @Override
    public boolean supports(String route) {
        return route.equals("/sessions");
    }

    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals("POST")) {
            return start(request);
        }
        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getMessage());
    }

    Response start(Request request) {
        User user = this.parser.getUserFromBody(request);

        Optional<User> foundUser = sessionService.login(user);
        if(foundUser.isPresent()) {
            user = foundUser.get();
            return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.OK, this.parser.getUserCredentials(user));
        }

        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED_ACCESS, HttpStatusMessage.UNAUTHORIZED_SESSION.getStatusMessage());
    }
}