package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.exceptions.HttpStatusException;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Test
    public void shouldUpdateUserData_whenUpdateUser() {
        // Arrange
        DatabaseUserRepository userRepository = mock(DatabaseUserRepository.class);
        UserService userService = new UserService(userRepository);
        AuthorizationService authorizationService = mock(AuthorizationService.class);
        userService.setAuthorizationService(authorizationService);

        User originalUser = new User("id","username", "password", "name", "bio", "image", 20, 100, 0, 0);
        User updatedUser = new User("id","username", "newPassword", "newName", "newBio", "newImage", 20, 100, 0, 0);
        String authorizationToken = "username-mtcgToken";
        String username = "username";

        when(authorizationService.getLoggedInUser(authorizationToken)).thenReturn(originalUser);
        when(userRepository.update(updatedUser)).thenReturn(updatedUser);

        // Act
        userService.update(authorizationToken, username, updatedUser);

        // Assert
        assertEquals("id", originalUser.getId());
        assertEquals("username", originalUser.getUsername());
        assertEquals("password", originalUser.getPassword());
        assertEquals("newName", originalUser.getName());
        assertEquals("newBio", originalUser.getBio());
        assertEquals("newImage", originalUser.getImage());
        assertEquals(20, originalUser.getCoins());
        assertEquals(100, originalUser.getElo());
        assertEquals(0, originalUser.getWins());
        assertEquals(0, originalUser.getLosses());
    }

    @Test
    public void shouldNotUpdateIdCoinsWinsLosses_whenUpdateUser() {
        // Arrange
        DatabaseUserRepository userRepository = mock(DatabaseUserRepository.class);
        UserService userService = new UserService(userRepository);
        AuthorizationService authorizationService = mock(AuthorizationService.class);
        userService.setAuthorizationService(authorizationService);

        User originalUser = new User("id","username", "password", "name", "bio", "image", 20, 100, 0, 0);
        User updatedUser = new User("id","username", "newPassword", "newName", "newBio", "newImage", 20, 100, 0, 0);
        String authorizationToken = "username-mtcgToken";
        String username = "username";

        when(authorizationService.getLoggedInUser(authorizationToken)).thenReturn(originalUser);
        when(userRepository.update(updatedUser)).thenReturn(updatedUser);

        // Act
        userService.update(authorizationToken, username, updatedUser);

        // Assert
        assertNotEquals("newId", originalUser.getId());
        assertEquals("username", originalUser.getUsername());
        assertEquals("password", originalUser.getPassword());
        assertEquals("newName", originalUser.getName());
        assertEquals("newBio", originalUser.getBio());
        assertEquals("newImage", originalUser.getImage());
        assertEquals(20, originalUser.getCoins());
        assertEquals(100, originalUser.getElo());
        assertEquals(0, originalUser.getWins());
        assertEquals(0, originalUser.getLosses());
    }

    @Test
    void shouldCallUserRepository_whenUpdateUser() {
        // Arrange
        DatabaseUserRepository userRepository = mock(DatabaseUserRepository.class);
        UserService userService = new UserService(userRepository);
        AuthorizationService authorizationService = mock(AuthorizationService.class);
        userService.setAuthorizationService(authorizationService);

        User user = new User("id","username", "newPassword", "newName", "newBio", "newImage", 20, 100, 0, 0);
        String authorizationToken = "username-mtcgToken";
        String username = "username";

        when(authorizationService.getLoggedInUser(authorizationToken)).thenReturn(user);

        // Act
        userService.update(authorizationToken, username, user);

        // Assert
        verify(userRepository, times(1)).update(user);
    }

    @Test
    void shouldSetUserId_whenSaveUser() {
        // Arrange
        DatabaseUserRepository userRepository = mock(DatabaseUserRepository.class);
        UserService userService = new UserService(userRepository);
        User user = new User("id","username", "password", "name", "bio", "image", 20, 100, 0, 0);

        when(userRepository.save(user)).thenReturn(user);

        // Act
        User savedUser = userService.save(user);

        // Assert
        assertNotEquals("", savedUser.getId());
        assertEquals("username", savedUser.getUsername());
        assertEquals("password", savedUser.getPassword());
        assertEquals("name", savedUser.getName());
        assertEquals("bio", savedUser.getBio());
        assertEquals("image", savedUser.getImage());
        assertEquals(20, savedUser.getCoins());
        assertEquals(100, savedUser.getElo());
        assertEquals(0, savedUser.getWins());
        assertEquals(0, savedUser.getLosses());
    }
    @Test
    void shouldCallUserRepository_whenSaveUser() {
        // Arrange
        DatabaseUserRepository userRepository = mock(DatabaseUserRepository.class);
        UserService userService = new UserService(userRepository);
        User user = new User("id","username", "newPassword", "newName", "newBio", "newImage", 20, 100, 0, 0);

        // Act
        userService.save(user);

        // Assert
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldThrowException_whenSaveAlreadyExistingUser() {
        // Arrange
        DatabaseUserRepository userRepository = mock(DatabaseUserRepository.class);
        UserService userService = new UserService(userRepository);
        User user = new User("id","username", "newPassword", "newName", "newBio", "newImage", 20, 100, 0, 0);

        when(userRepository.findWithUsername(user.getUsername())).thenReturn(Optional.of(user));

        assertThrows(HttpStatusException.class, () -> {
            userService.save(user);
        });
    }
}
