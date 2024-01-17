package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Package;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.exceptions.ExceptionMessage;
import at.technikum.apps.mtcg.exceptions.HttpStatusException;
import at.technikum.apps.mtcg.repository.DatabasePackageRepository;
import at.technikum.apps.mtcg.repository.DatabaseCardRepository;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;

import java.sql.SQLException;
import java.util.Optional;

public class TransactionService {
    private final AuthorizationService authorizationService;
    private final DatabaseCardRepository databaseCardRepository;
    private final DatabasePackageRepository databasePackageRepository;

    public TransactionService(AuthorizationService authorizationService, DatabaseCardRepository databaseCardRepository, DatabasePackageRepository databasePackageRepository) {
        this.authorizationService = authorizationService;
        this.databaseCardRepository = databaseCardRepository;
        this.databasePackageRepository = databasePackageRepository;
    }

    public Package buyPackage(String authorizationToken, UserService userService) {
        User user = this.authorizationService.getLoggedInUser(authorizationToken);
        if(user.getCoins() < 5) {
            throw new HttpStatusException(HttpStatus.FORBIDDEN, HttpContentType.TEXT_PLAIN, ExceptionMessage.FORBIDDEN_TRANSACTION);
        }

        Optional<Package> cardPackage = databasePackageRepository.getPackage();
        if(cardPackage.isEmpty()) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, HttpContentType.TEXT_PLAIN, ExceptionMessage.NOT_FOUND_TRANSACTION);
        }

        Package selectedCardPackage = cardPackage.get();
        selectedCardPackage.setCards(this.databaseCardRepository.getCardsFromPackage(selectedCardPackage.getId()));

        for(Card card : selectedCardPackage.getCards()) {
            this.databaseCardRepository.updateCardOwner(card.getId(), user.getId());
        }

        userService.updateCoins(user, user.getCoins() - 5);
        this.databasePackageRepository.deletePackage(selectedCardPackage.getId());

        return selectedCardPackage;
    }
}
