package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.*;

import java.util.Objects;
import java.util.Optional;

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
        Optional<User> userOptional = userService.findWithUsername(username);

        if(userOptional.isEmpty()) {
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.NOT_FOUND, HttpStatusMessage.NOT_FOUND_USER.getStatusMessage());
        }

        User user = userOptional.get();

        if(Objects.equals(request.getAuthorizationToken(), user.getToken()) && user.getToken() != null) {
            return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.OK, this.parser.getUserCredentials(user));
        }

        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED_ACCESS.getMessage());
    }

    Response update(String username, Request request) {
        Optional<User> userOptional = userService.findWithUsername(username);

        if(userOptional.isEmpty()) {
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.NOT_FOUND,HttpStatusMessage.NOT_FOUND_USER.getStatusMessage());
        }

        User currentUser = userOptional.get();

        if((Objects.equals(request.getAuthorizationToken(), currentUser.getToken()) || Objects.equals(request.getAuthorizationToken(), "admin-mtcgToken")) && currentUser.getToken() != null) {
            User updatedUser = this.parser.getUserFromBody(request);
            updatedUser = userService.update(currentUser, updatedUser);
            if(updatedUser != null) {
                return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.OK, this.parser.getUserData(updatedUser));
            }
        }
        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.INTERNAL_ERROR, HttpStatus.INTERNAL_ERROR.getMessage());
    }

    Response create(Request request) {

        User user = this.parser.getUserFromBody(request);

        Optional<User> userOptional= userService.findWithUsername(user.getUsername());

        if(userOptional.isPresent()) {
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.ALREADY_EXISTS,HttpStatusMessage.ALREADY_EXISTS_USER.getStatusMessage());
        }

        user = userService.save(user);

        return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.CREATED, this.parser.getUserCredentials(user));
    }
}
