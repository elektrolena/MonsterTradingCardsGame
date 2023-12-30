package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

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
                    return read(username);
                case "PUT":
                    return update(username, request);
            }
        } else if (route.equals("/users")) {
            if(request.getMethod().equals("POST")) {
                return create(request);
            }
        }

        return status(HttpStatus.BAD_REQUEST);
    }

    // TODO: change username to token
    public Response read(String username) {
        Optional<User> userOptional = userService.find(username);

        if(userOptional.isEmpty()) {
            return createFailureResponse("User not found in app!");
        }

        User user = userOptional.get();
        return createSuccessJsonResponse(user, HttpStatus.OK);
    }

    // TODO: change username to token
    private Response update(String username, Request request) {
        Optional<User> userOptional = userService.find(username);

        if(userOptional.isEmpty()) {
            return createFailureResponse("User not found in app!");
        }

        User currentUser = userOptional.get();

        User updatedUser = getUserFromBody(request);

        updatedUser = userService.update(currentUser, updatedUser);

        return createSuccessJsonResponse(updatedUser, HttpStatus.OK);
    }

    public Response create(Request request) {

        User user = getUserFromBody(request);

        Optional<User> userOptional= userService.find(user.getUsername());

        if(userOptional.isPresent()) {
            return createFailureResponse("Username is already taken!");
        }

        user = userService.save(user);

        return createSuccessJsonResponse(user, HttpStatus.CREATED);
    }
}
