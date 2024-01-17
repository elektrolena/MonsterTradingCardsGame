package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.*;

public class UserController extends Controller {

    private final UserService userService;

    public UserController(JsonParser parser, UserService userService) {
        super(parser);
        this.userService = userService;
    }
    @Override
    public boolean supports(String route) {
        return route.matches("/users/\\w+") || route.equals("/users");
    }

    @Override
    public Response handle(Request request) {
        String route = request.getRoute();

        if (route.matches("/users/\\w+")) {
            String username = extractLastRoutePart(route);
            switch(request.getMethod()) {
                case "GET":
                    return read(username, request);
                case "PUT":
                    return update(username, request);
            }
        } else if (route.equals("/users")) {
            if(request.getMethod().equals("POST")) {
                return create(request);
            }
        }

        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getMessage());
    }

    Response read(String username, Request request) {
        User user = userService.findWithUsername(username, request.getAuthorizationToken());

        return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.OK, this.parser.getUserData(user));
    }

    Response update(String username, Request request) {
        User updatedUser = this.parser.getUserFromBody(request);
        updatedUser = userService.update(request.getAuthorizationToken(), username, updatedUser);

        return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.OK, this.parser.getUserData(updatedUser));
    }


    Response create(Request request) {
        User user = this.parser.getUserFromBody(request);

        user = userService.save(user);

        return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.CREATED, this.parser.getUserCredentials(user));
    }
}
