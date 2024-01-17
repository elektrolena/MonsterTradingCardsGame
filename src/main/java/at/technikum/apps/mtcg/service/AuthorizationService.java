package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.exceptions.ExceptionMessage;
import at.technikum.apps.mtcg.exceptions.HttpStatusException;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;

public class AuthorizationService {
    private final UserService userService;

    public AuthorizationService(UserService userService) {
        this.userService = userService;
    }

    public User getLoggedInUser(String authorizationToken) {
        if(authorizationToken == null) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED_ACCESS, HttpContentType.TEXT_PLAIN, ExceptionMessage.UNAUTHORIZED_ACCESS);
        }

        return this.userService.findWithToken(authorizationToken);
    }
}
