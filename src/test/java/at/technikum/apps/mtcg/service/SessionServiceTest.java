package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SessionServiceTest {
    @Test
    public void whenLoginSuccessful_ShouldReturnOptionalOfUser() {
        // Arrange
        DatabaseUserRepository databaseUserRepository = mock(DatabaseUserRepository.class);
        SessionService sessionService = new SessionService(databaseUserRepository);
        User user = new User("id", "username", "password", "bio", "image", 20, 100, 0, 0);

        when(databaseUserRepository.validateLogin(user)).thenReturn(Optional.of(user));

        // Act
        Optional<User> loggedInUser = sessionService.login(user);

        // Assert
        assertTrue(loggedInUser.isPresent());
        User finalUser = loggedInUser.get();
        assertEquals("id", finalUser.getId());
        assertEquals("username", finalUser.getUsername());
        assertEquals("password", finalUser.getPassword());
        assertEquals("username-mtcgToken", finalUser.getToken());
        assertEquals("bio", finalUser.getBio());
        assertEquals("image", finalUser.getImage());
        assertEquals(20, finalUser.getCoins());
        assertEquals(100, finalUser.getElo());
        assertEquals(0, finalUser.getWins());
        assertEquals(0, finalUser.getLosses());
    }

    @Test
    public void whenLoginNotSuccessful_ShouldReturnOptionalEmpty() {
        // Arrange
        DatabaseUserRepository databaseUserRepository = mock(DatabaseUserRepository.class);
        SessionService sessionService = new SessionService(databaseUserRepository);
        User user = new User("id", "username", "password", "bio", "image", 20, 100, 0, 0);

        when(databaseUserRepository.validateLogin(user)).thenReturn(Optional.empty());

        // Act
        Optional<User> loggedInUser = sessionService.login(user);

        // Assert
        assertFalse(loggedInUser.isPresent());
    }
}
