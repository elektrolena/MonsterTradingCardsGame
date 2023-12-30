package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.util.Objects;
import java.util.Optional;

public class UserController extends Controller {

    private final UserService userService;


    public UserController() {
        this.userService = new UserService(new DatabaseUserRepository());
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

    public Response read(String username, Request request) {
        Optional<User> userOptional = userService.find(username);

        if(userOptional.isEmpty()) {
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.NOT_FOUND,"User not found in app!");
        }

        User user = userOptional.get();

        if(Objects.equals(request.getAuthorizationToken(), user.getToken())) {
            return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.OK, convertUserObjectToJson(user));
        }

        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED_ACCESS.getMessage());
    }

    // TODO: add admin access?
    private Response update(String username, Request request) {
        Optional<User> userOptional = userService.find(username);

        if(userOptional.isEmpty()) {
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.NOT_FOUND,"User not found in app!");
        }

        User currentUser = userOptional.get();

        if(Objects.equals(request.getAuthorizationToken(), currentUser.getToken())) {
            User updatedUser = getUserFromBody(request);
            updatedUser = userService.update(currentUser, updatedUser);
            return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.OK, convertUserObjectToJson(updatedUser));
        }

        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED_ACCESS.getMessage());
    }

    public Response create(Request request) {

        User user = getUserFromBody(request);

        Optional<User> userOptional= userService.find(user.getUsername());

        if(userOptional.isPresent()) {
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.ALREADY_EXISTS,"User with same username already registered!");
        }

        user = userService.save(user);

        return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.CREATED, convertUserObjectToJson(user));
    }
}
