package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.util.Optional;

public class StatsController extends Controller {

    private final UserService userService;

    public StatsController() {
        super();
        this.userService = new UserService(new DatabaseUserRepository());
    }

    @Override
    public boolean supports(String route) {
        return route.equals("/stats") || route.equals("/scoreboard");
    }

    @Override
    public Response handle(Request request) {
        if(request.getRoute().equals("/stats")) {
            if(request.getMethod().equals("GET")) {
                return getUserStats(request);
            }
        } else if(request.getRoute().equals("/scoreboard")) {
            if(request.getMethod().equals("GET")) {
                return getScoreBoard(request);
            }
        }
        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getMessage());
    }

    private Response getUserStats(Request request) {
        Optional<User> optionalUser = checkForAuthorizedRequest(request, userService);
        if(optionalUser.isEmpty()){
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED_ACCESS.getMessage());
        }

        User user = optionalUser.get();
        return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.OK, this.parser.getUserStats(user));
    }

    private Response getScoreBoard(Request request) {
        Optional<User> optionalUser = checkForAuthorizedRequest(request, userService);
        if(optionalUser.isEmpty()){
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED_ACCESS.getMessage());
        }

        User user = optionalUser.get();
        return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.OK, this.parser.getUsers(this.userService.getUserScoreBoard()));
    }
}
