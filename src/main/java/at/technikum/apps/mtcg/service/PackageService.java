package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Package;
import at.technikum.apps.mtcg.repository.DatabaseCardRepository;
import at.technikum.apps.mtcg.repository.DatabasePackageRepository;
import at.technikum.server.http.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

public class PackageService {

    private final DatabaseCardRepository databaseCardRepository;
    private final DatabasePackageRepository databasePackageRepository;

    public PackageService(DatabaseCardRepository databaseCardRepository, DatabasePackageRepository databasePackageRepository) {
        this.databaseCardRepository = databaseCardRepository;
        this.databasePackageRepository = databasePackageRepository;
    }

    public boolean save(Card[] cards) {

        for(Card card : cards) {
            if(this.databaseCardRepository.findWithId(card.getId()).isPresent()) {
                return false;
            }
        }

        Package cardPackage = new Package();
        cardPackage.setId(UUID.randomUUID().toString());
        cardPackage.setPrice(5);

        this.databasePackageRepository.savePackage(cardPackage);

        for (Card card : cards) {
            card.setPackageId(cardPackage.getId());
            this.databaseCardRepository.save(card);
        }

        return true;
    }
}
