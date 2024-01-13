package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.exceptions.HttpStatusException;
import at.technikum.apps.mtcg.exceptions.ExceptionMessage;
import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.server.http.*;

import java.util.Optional;

public abstract class Service {
    protected final JsonParser parser;

    protected Service(JsonParser parser) {
        this.parser = parser;
    }
    protected User getLoggedInUser(Request request, UserService userService) {
        if(request.getAuthorizationToken() == null) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED_ACCESS, HttpContentType.TEXT_PLAIN, ExceptionMessage.UNAUTHORIZED_ACCESS);
        }
        Optional<User> user = userService.findWithToken(request.getAuthorizationToken());
        if(user.isEmpty()) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED_ACCESS, HttpContentType.TEXT_PLAIN, ExceptionMessage.UNAUTHORIZED_ACCESS);
        }

        return user.get();
    }
}
