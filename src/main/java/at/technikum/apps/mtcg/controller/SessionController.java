package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import at.technikum.apps.mtcg.service.SessionService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.util.Optional;

public class SessionController extends Controller {

    private final SessionService sessionService;


    public SessionController() {
        this.sessionService = new SessionService(new DatabaseUserRepository());
    }

    @Override
    public boolean supports(String route) {
        return route.equals("/sessions");
    }

    @Override
    public Response handle(Request request) {
        String route = request.getRoute();

        if (route.equals("/sessions")) {
            if (request.getMethod().equals("POST")) {
                return start(request);
            }
        }

        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getMessage());
    }

    private Response start(Request request) {
        User user = getUserFromBody(request);

        Optional<User> foundUser = sessionService.login(user);
        if(foundUser.isPresent()) {
            user = foundUser.get();
            return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.OK, convertUserObjectToJson(user));
        }

        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED_ACCESS, "Invalid username/password provided.");
    }
}