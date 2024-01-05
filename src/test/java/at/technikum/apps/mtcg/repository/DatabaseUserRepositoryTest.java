package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseUserRepositoryTest {

    @Test
    public void whenAddOneUser_ThenFindShouldReturnUser() {
        // Arrange
        DatabaseUserRepository databaseUserRepositoryMock = mock(DatabaseUserRepository.class);

        User userToAdd = new User("id", "username", "password", "bio", "image", 20, 100, 0, 0);

        when(databaseUserRepositoryMock.save(userToAdd)).thenReturn(userToAdd);

        when(databaseUserRepositoryMock.findWithUsername("username")).thenReturn(Optional.of(userToAdd));

        // Act
        databaseUserRepositoryMock.save(userToAdd);
        Optional<User> retrievedUserMock = databaseUserRepositoryMock.findWithUsername("username");

        // Assert
        assertTrue(retrievedUserMock.isPresent());
        User userMock = retrievedUserMock.get();
        assertEquals("username", userMock.getUsername());
        assertEquals("password", userMock.getPassword());
        assertEquals("bio", userMock.getBio());
        assertEquals("image", userMock.getImage());
        assertEquals(20, userMock.getCoins());
        assertEquals(100, userMock.getElo());
        assertEquals(0, userMock.getWins());
        assertEquals(0, userMock.getLosses());
    }

    @Test
    public void whenAddOneUser_ThenFindShouldNotReturnFalseUserCredentials() {
        // Arrange
        DatabaseUserRepository databaseUserRepositoryMock = mock(DatabaseUserRepository.class);

        User userToAdd = new User("id", "username", "password", "bio", "image", 20, 100, 0, 0);

        when(databaseUserRepositoryMock.save(userToAdd)).thenReturn(userToAdd);

        when(databaseUserRepositoryMock.findWithUsername("username")).thenReturn(Optional.of(userToAdd));

        // Act
        databaseUserRepositoryMock.save(userToAdd);
        Optional<User> retrievedUserMock = databaseUserRepositoryMock.findWithUsername("username");

        // Assert
        assertTrue(retrievedUserMock.isPresent());
        User userMock = retrievedUserMock.get();
        assertNotEquals("falseUsername", userMock.getUsername());
        assertNotEquals("falsePassword", userMock.getPassword());
        assertNotEquals("falseBio", userMock.getBio());
        assertNotEquals("falseImage", userMock.getImage());
        assertNotEquals(0, userMock.getCoins());
        assertNotEquals(0, userMock.getElo());
        assertNotEquals(10, userMock.getWins());
        assertNotEquals(20, userMock.getLosses());
    }

    @Test
    public void whenUpdateOneUser_ThenFindShouldReturnNewUser() {
        // Arrange
        DatabaseUserRepository databaseUserRepositoryMock = mock(DatabaseUserRepository.class);

        User userToAdd = new User("id", "username", "password", "bio", "image", 20, 100, 0, 0);
        User updatedUser = new User("id", "username", "newPassword", "newBio", "image", 20, 100, 0, 0);

        when(databaseUserRepositoryMock.save(userToAdd)).thenReturn(userToAdd);
        when(databaseUserRepositoryMock.update(updatedUser)).thenReturn(updatedUser);
        when(databaseUserRepositoryMock.findWithUsername("username")).thenReturn(Optional.of(updatedUser));

        // Act
        databaseUserRepositoryMock.save(userToAdd);
        databaseUserRepositoryMock.update(updatedUser);
        Optional<User> retrievedUpdatedUserMock = databaseUserRepositoryMock.findWithUsername("username");

        // Assert
        assertTrue(retrievedUpdatedUserMock.isPresent());
        User userMock = retrievedUpdatedUserMock.get();
        assertEquals("username", userMock.getUsername());
        assertEquals("newPassword", userMock.getPassword());
        assertEquals("newBio", userMock.getBio());
        assertEquals("image", userMock.getImage());
        assertEquals(20, userMock.getCoins());
        assertEquals(100, userMock.getElo());
        assertEquals(0, userMock.getWins());
        assertEquals(0, userMock.getLosses());
    }

    @Test
    public void whenUpdateOneUser_ThenFindShouldNotReturnOldUser() {
        // Arrange
        DatabaseUserRepository databaseUserRepositoryMock = mock(DatabaseUserRepository.class);

        User userToAdd = new User("id", "username", "password", "bio", "image", 20, 100, 0, 0);
        User updatedUser = new User("id", "username", "newPassword", "newBio", "image", 20, 100, 0, 0);

        when(databaseUserRepositoryMock.save(userToAdd)).thenReturn(userToAdd);
        when(databaseUserRepositoryMock.update(updatedUser)).thenReturn(updatedUser);
        when(databaseUserRepositoryMock.findWithUsername("username")).thenReturn(Optional.of(updatedUser));

        // Act
        databaseUserRepositoryMock.save(userToAdd);
        databaseUserRepositoryMock.update(updatedUser);
        Optional<User> retrievedUpdatedUserMock = databaseUserRepositoryMock.findWithUsername("username");

        // Assert
        assertTrue(retrievedUpdatedUserMock.isPresent());
        User userMock = retrievedUpdatedUserMock.get();
        assertEquals("username", userMock.getUsername());
        assertNotEquals("password", userMock.getPassword());
        assertNotEquals("bio", userMock.getBio());
        assertEquals("image", userMock.getImage());
        assertEquals(20, userMock.getCoins());
        assertEquals(100, userMock.getElo());
        assertEquals(0, userMock.getWins());
        assertEquals(0, userMock.getLosses());
    }
}
