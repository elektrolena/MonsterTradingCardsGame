package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class UserController implements Controller {

    private final UserService userService;

    public UserController() {
        this.userService = new UserService(new DatabaseUserRepository());
    }
    @Override
    public boolean supports(String route) {
        return route.equals("/users");
    }

    @Override
    public Response handle(Request request) {

        if(request.getRoute().equals("/users")) {
            switch (request.getMethod()) {
                case "GET": return readAll(request);
                case "POST": return create(request);
            }

            // THOUGHT: better 405
            return status(HttpStatus.BAD_REQUEST);
        }

        String[] routeParts = request.getRoute().split("/");
        int userId = Integer.parseInt(routeParts[2]);

        switch (request.getMethod()) {
            case "GET": return read(userId, request);
            case "PUT": return update(userId, request);
            case "DELETE": return delete(userId, request);
        }

        // THOUGHT: better 405
        return status(HttpStatus.BAD_REQUEST);
    }

    public Response create(Request request) {

        ObjectMapper objectmapper = new ObjectMapper();
        User user = null;
        try {
            user = objectmapper.readvalue(request.getBody(), User.class)
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // user = toObject(request.getBody(), User.class);

        user = userService.save(user);

        String taskJson = null;
        try {
            taskJson = objectmapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Response response = new Response();
        // THOUGHT: better status 201 Created
        response.setStatus(HttpStatus.OK);
        response.setContentType(HttpContentType.TEXT_PLAIN);
        response.setBody(taskJson);

        return response;

        // return json(user);
    }

    public Response readAll(Request request) {
        List<User> users = userService.findAll();

        ObjectMapper objectMapper = new ObjectMapper();
        String tasksJson = null;
        try {
            tasksJson = objectMapper.writeValueAsString(users);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Object to JSON coming soon

        Response response = new Response();
        // THOUGHT: better status 201 Created
        response.setStatus(HttpStatus.OK);
        response.setContentType(HttpContentType.APPLICATION_JSON);
        response.setBody(tasksJson);

        return response;
    }

    public Response read(int id, Request request) {
        return null;
    }

    public Response update(int id, Request request) {
        return null;
    }

    public Response delete(int id, Request request) {
        return null;
    }
}
