package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.repository.DatabaseCardRepository;
import at.technikum.apps.mtcg.repository.DatabasePackageRepository;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PackageServiceTest {
    @Test
    public void shouldReturnTrue_WhenSavedCards() throws SQLException {
        // Arrange
        DatabaseCardRepository databaseCardRepository = mock(DatabaseCardRepository.class);
        DatabasePackageRepository databasePackageRepository = mock(DatabasePackageRepository.class);
        PackageService packageService = new PackageService(databaseCardRepository, databasePackageRepository);

        Card[] cards = new Card[1];
        Card card = new Card("id", "name", "element", "type", 50, 1, "ownerId", "packageId");
        cards[0] = card;

        when(databaseCardRepository.findWithId(card.getId())).thenReturn(Optional.empty());

        // Act
        boolean success = packageService.save(cards);

        // Assert
        assertTrue(success);
    }

    @Test
    public void shouldReturnFalse_WhenNotSavedCards() throws SQLException {
        // Arrange
        DatabaseCardRepository databaseCardRepository = mock(DatabaseCardRepository.class);
        DatabasePackageRepository databasePackageRepository = mock(DatabasePackageRepository.class);
        PackageService packageService = new PackageService(databaseCardRepository, databasePackageRepository);

        Card[] cards = new Card[1];
        Card card = new Card("id", "name", "element", "type", 50, 1, "ownerId", "packageId");
        cards[0] = card;
        
        when(databaseCardRepository.findWithId(card.getId())).thenReturn(Optional.of(card));

        // Act
        boolean success = packageService.save(cards);

        // Assert
        assertFalse(success);
    }
}
