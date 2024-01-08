package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Package;
import at.technikum.apps.mtcg.repository.DatabaseCardRepository;
import at.technikum.apps.mtcg.repository.DatabasePackageRepository;
import at.technikum.server.http.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class PackageService {

    private final DatabaseCardRepository databaseCardRepository;
    private final DatabasePackageRepository databasePackageRepository;

    public PackageService(DatabaseCardRepository databaseCardRepository, DatabasePackageRepository databasePackageRepository) {
        this.databaseCardRepository = databaseCardRepository;
        this.databasePackageRepository = databasePackageRepository;
    }

    public boolean save(Card[] cards) throws SQLException {

        for(Card card : cards) {
            Optional<Card> existingCard = this.databaseCardRepository.findWithId(card.getId());
            if (existingCard.isPresent()) {
                return false;
            }
        }

        if (cards.length > 0) {
            Package cardPackage = new Package();
            cardPackage.setId(UUID.randomUUID().toString());
            cardPackage.setPrice(5);

            this.databasePackageRepository.savePackage(cardPackage);

            for (Card card : cards) {
                card.setPackageId(cardPackage.getId());
                card.setElement(parseElementFromName(card.getName()));
                card.setType(parseTypeFromName(card.getName()));
                this.databaseCardRepository.save(card);
            }
            return true;
        }
        return false;
    }

    private String parseElementFromName(String name) {
        if (name.contains("Water")) {
            return "water";
        } else if (name.contains("Fire")) {
            return "fire";
        } else {
            return null;
        }
    }

    private String parseTypeFromName(String name) {
        if(name.contains("Spell")) {
            return "spell";
        } else {
            return "monster";
        }
    }
}
