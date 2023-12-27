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

import java.util.List;
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
            switch(request.getMethod()) {
                case "GET":
                    String username = extractUsername(route);
                    return read(username, request);
            }
        } else if (route.equals("/users")) {
            if(request.getMethod().equals("POST")) {
                return create(request);
            }
        }

        return status(HttpStatus.BAD_REQUEST);
    }

    private String extractUsername(String route) {
        String[] routeParts = route.split("/");
        return routeParts[2];
    }


    public Response create(Request request) {

        ObjectMapper objectmapper = new ObjectMapper();
        User user = null;
        try {
            user = objectmapper.readValue(request.getBody(), User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        user = userService.save(user);

        String taskJson = null;
        try {
            taskJson = objectmapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Response response = new Response();
        response.setStatus(HttpStatus.CREATED);
        response.setContentType(HttpContentType.TEXT_PLAIN);
        response.setBody(taskJson);

        return response;
    }

    public Response read(String username, Request request) {

        Optional<User> userOptional= userService.find(username);

        Response response = new Response();
        if(userOptional.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody("User '" + username + "' not found in app!");
            return response;
        }

        User user = userOptional.get();

        // dunno if I will need this as the return string should be "Data successfully retrieved"
        ObjectMapper objectMapper = new ObjectMapper();
        String tasksJson = null;
        try {
            tasksJson = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        response.setStatus(HttpStatus.OK);
        response.setContentType(HttpContentType.APPLICATION_JSON);
        response.setBody(tasksJson);
        return response;
    }
}
