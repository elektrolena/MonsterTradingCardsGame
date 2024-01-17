package at.technikum.apps.mtcg.controller;
import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.service.TransactionService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class UserControllerTest {

    @Test
    public void shouldSupportRoute_WhenValidUserRoute() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(parser, userService);
        String firstRoute = "/users";
        String secondRoute = "/users/username";
        boolean doesSupportFirstRoute = false;
        boolean doesSupportSecondRoute = false;

        // Act
        doesSupportFirstRoute = userController.supports(firstRoute);
        doesSupportSecondRoute = userController.supports(secondRoute);

        // Assert
        assertTrue(doesSupportFirstRoute);
        assertTrue(doesSupportSecondRoute);
    }

    @Test
    public void shouldNotSupportRoute_WhenInvalidUserRoute() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(parser, userService);
        String route = "/sessions";
        boolean doesSupport = false;

        // Act
        doesSupport = userController.supports(route);

        // Assert
        assertFalse(doesSupport);
    }

    @Test
    public void shouldSupportRequestMethod_WhenValidUserMethod() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        UserService userService = mock(UserService.class);
        UserController userController = spy(new UserController(parser, userService));
        Request getRequest = mock(Request.class);
        Request putRequest = mock(Request.class);
        Request postRequest = mock(Request.class);
        Response response = mock(Response.class);
        String username = "username";

        when(getRequest.getRoute()).thenReturn("/users/username");
        when(getRequest.getMethod()).thenReturn("GET");

        when(putRequest.getRoute()).thenReturn("/users/username");
        when(putRequest.getMethod()).thenReturn("PUT");

        when(postRequest.getRoute()).thenReturn("/users");
        when(postRequest.getMethod()).thenReturn("POST");

        doReturn(response).when(userController).read(username, getRequest);
        doReturn(response).when(userController).update(username, putRequest);
        doReturn(response).when(userController).create(postRequest);

        // Act
        userController.handle(getRequest);
        userController.handle(putRequest);
        userController.handle(postRequest);

        // Assert
        verify(userController, times(1)).read(username, getRequest);
        verify(userController, times(1)).update(username, putRequest);
        verify(userController, times(1)).create(postRequest);
    }

    @Test
    public void shouldNotSupportRequestMethod_WhenInValidDeckMethod() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        UserService userService = mock(UserService.class);
        UserController userController = spy(new UserController(parser, userService));
        Request request = mock(Request.class);
        Response response = mock(Response.class);
        String username = "username";

        when(request.getRoute()).thenReturn("/users");
        when(request.getMethod()).thenReturn("DELETE");

        doReturn(response).when(userController).read(username, request);
        doReturn(response).when(userController).update(username, request);
        doReturn(response).when(userController).create(request);

        // Act
        userController.handle(request);

        // Assert
        verify(userController, times(0)).read(username, request);
        verify(userController, times(0)).update(username, request);
        verify(userController, times(0)).create(request);
    }
}
