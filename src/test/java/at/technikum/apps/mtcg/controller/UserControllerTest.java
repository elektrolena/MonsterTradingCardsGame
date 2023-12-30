package at.technikum.apps.mtcg.controller;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    @Test
    public void shouldSupportRoute_WhenValidUserRoute() {
        // Arrange
        UserController userController = new UserController();
        String route = "/users";
        boolean doesSupport = false;

        // Act
        doesSupport = userController.supports(route);

        // Assert
        assertTrue(doesSupport);
    }

    @Test
    public void shouldNotSupportRoute_WhenInvalidUserRoute() {
        // Arrange
        UserController userController = new UserController();
        String route = "/session";
        boolean doesSupport = false;

        // Act
        doesSupport = userController.supports(route);

        // Assert
        assertFalse(doesSupport);
    }
}
