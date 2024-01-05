package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Test
    public void shouldUpdateUser_whenUpdateUser() {
        // Arrange
        DatabaseUserRepository userRepository = mock(DatabaseUserRepository.class);
        UserService userService = new UserService(userRepository);
        User originalUser = new User("id","username", "password", "bio", "image", 20, 100, 0, 0);
        User updatedUser = new User("id","username", "newPassword", "newBio", "newImage", 20, 100, 0, 0);

        when(userRepository.update(updatedUser)).thenReturn(updatedUser);

        // Act
        userService.update(originalUser, updatedUser);

        // Assert
        assertEquals("id", originalUser.getId());
        assertEquals("username", originalUser.getUsername());
        assertEquals("newPassword", originalUser.getPassword());
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
        User originalUser = new User("id","username", "password", "bio", "image", 20, 100, 0, 0);
        User updatedUser = new User("newId","username", "newPassword", "newBio", "newImage", 0, 500, 10, 5);

        when(userRepository.update(updatedUser)).thenReturn(updatedUser);

        // Act
        userService.update(originalUser, updatedUser);

        // Assert
        assertNotEquals("newId", originalUser.getId());
        assertEquals("username", originalUser.getUsername());
        assertEquals("newPassword", originalUser.getPassword());
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
        User user = new User("","username", "password", "bio", "image", 20, 100, 0, 0);
        User updatedUser = new User("id","username", "newPassword", "newBio", "newImage", 20, 100, 0, 0);

        // Act
        userService.update(user, updatedUser);

        // Assert
        verify(userRepository, times(1)).update(user);
    }

    @Test
    void shouldSetUserId_whenSaveUser() {
        // Arrange
        DatabaseUserRepository userRepository = mock(DatabaseUserRepository.class);
        UserService userService = new UserService(userRepository);
        User user = new User("","username", "password", "bio", "image", 20, 100, 0, 0);

        when(userRepository.save(user)).thenReturn(user);

        // Act
        User savedUser = userService.save(user);

        // Assert
        assertNotEquals("", savedUser.getId());
        assertEquals("username", savedUser.getUsername());
        assertEquals("password", savedUser.getPassword());
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
        User user = new User("","username", "password", "bio", "image", 20, 100, 0, 0);

        // Act
        userService.save(user);

        // Assert
        verify(userRepository, times(1)).save(user);
    }
}
