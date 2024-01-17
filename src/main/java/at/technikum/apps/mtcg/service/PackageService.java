package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Package;
import at.technikum.apps.mtcg.exceptions.ExceptionMessage;
import at.technikum.apps.mtcg.exceptions.HttpStatusException;
import at.technikum.apps.mtcg.repository.DatabaseCardRepository;
import at.technikum.apps.mtcg.repository.DatabasePackageRepository;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PackageService {

    private final AuthorizationService authorizationService;
    private final DatabaseCardRepository databaseCardRepository;
    private final DatabasePackageRepository databasePackageRepository;

    public PackageService(AuthorizationService authorizationService, DatabaseCardRepository databaseCardRepository, DatabasePackageRepository databasePackageRepository) {
        this.authorizationService = authorizationService;
        this.databaseCardRepository = databaseCardRepository;
        this.databasePackageRepository = databasePackageRepository;
    }

    public void save(String authorizationToken, List<Card> cards) {
        User admin = this.authorizationService.getLoggedInUser(authorizationToken);

        if(!admin.getUsername().equals("admin") || !admin.getToken().equals("admin-mtcgToken")) {
            throw new HttpStatusException(HttpStatus.FORBIDDEN, HttpContentType.TEXT_PLAIN, ExceptionMessage.FORBIDDEN_PACKAGE);
        }
        if (cards.size() != 5) {
            throw new HttpStatusException(HttpStatus.FORBIDDEN, HttpContentType.TEXT_PLAIN, ExceptionMessage.FORBIDDEN_PACKAGE);
        }

        for(Card card : cards) {
            Optional<Card> existingCard = this.databaseCardRepository.findWithId(card.getId());
            if (existingCard.isPresent()) {
                throw new HttpStatusException(HttpStatus.ALREADY_EXISTS, HttpContentType.TEXT_PLAIN, ExceptionMessage.ALREADY_EXISTS_PACKAGE);
            }
        }
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
    }

    private String parseElementFromName(String name) {
        if (name.contains("Water")) {
            return "water";
        } else if (name.contains("Fire")) {
            return "fire";
        } else {
            return "normal";
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
