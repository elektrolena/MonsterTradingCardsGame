package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.service.StatsService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.util.List;

public class StatsController extends Controller {
    private final UserService userService;
    private final StatsService statsService;

    public StatsController(JsonParser parser, UserService userService, StatsService statsService) {
        super(parser);
        this.userService = userService;
        this.statsService = statsService;
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

    Response getUserStats(Request request) {
        User user = userService.findWithToken(request.getAuthorizationToken());

        return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.OK, this.parser.getUserStats(user));
    }

    Response getScoreBoard(Request request) {
        List<User> users = this.statsService.getUserScoreBoard(request.getAuthorizationToken());

        return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.OK, this.parser.getUsers(users));
    }
}
