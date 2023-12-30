package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
            String username = extractUsername(route);
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

    private User getUserFromBody(Request request) {
        ObjectMapper objectmapper = new ObjectMapper();
        User user = null;
        try {
            user = objectmapper.readValue(request.getBody(), User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    private Response createSuccessJsonResponse(User user, HttpStatus status) {
        Response response = new Response();
        ObjectMapper objectmapper = new ObjectMapper();

        String taskJson = null;
        try {
            taskJson = objectmapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        response.setStatus(status);
        response.setContentType(HttpContentType.APPLICATION_JSON);
        response.setBody(taskJson);

        return response;
    }

    private Response createFailureResponse(String body) {
        Response response = new Response();

        response.setStatus(HttpStatus.NOT_FOUND);
        response.setContentType(HttpContentType.TEXT_PLAIN);
        response.setBody(body);

        return response;
    }

    private String extractUsername(String route) {
        String[] routeParts = route.split("/");
        return routeParts[2];
    }
}
