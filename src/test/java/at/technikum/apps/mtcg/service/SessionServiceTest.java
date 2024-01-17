package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.exceptions.HttpStatusException;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SessionServiceTest {
    @Test
    public void whenLoginSuccessful_ShouldReturnUser() {
        // Arrange
        DatabaseUserRepository databaseUserRepository = mock(DatabaseUserRepository.class);
        SessionService sessionService = new SessionService(databaseUserRepository);
        User user = new User("id", "username", "password", "name", "bio", "image", 20, 100, 0, 0);

        when(databaseUserRepository.validateLogin(user)).thenReturn(Optional.of(user));

        // Act
        User loggedInUser = sessionService.login(user);

        // Assert
        assertEquals("id", loggedInUser.getId());
        assertEquals("username", loggedInUser.getUsername());
        assertEquals("password", loggedInUser.getPassword());
        assertEquals("username-mtcgToken", loggedInUser.getToken());
        assertEquals("bio", loggedInUser.getBio());
        assertEquals("image", loggedInUser.getImage());
        assertEquals(20, loggedInUser.getCoins());
        assertEquals(100, loggedInUser.getElo());
        assertEquals(0, loggedInUser.getWins());
        assertEquals(0, loggedInUser.getLosses());
    }

    @Test
    public void whenLoginNotSuccessful_ShouldThrowException() {
        // Arrange
        DatabaseUserRepository databaseUserRepository = mock(DatabaseUserRepository.class);
        SessionService sessionService = new SessionService(databaseUserRepository);
        User user = new User("id", "username", "password", "name", "bio", "image", 20, 100, 0, 0);

        when(databaseUserRepository.validateLogin(user)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(HttpStatusException.class, () -> {
            sessionService.login(user);
        });
    }
}
