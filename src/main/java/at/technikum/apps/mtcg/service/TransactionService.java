package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Package;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabasePackageRepository;
import at.technikum.apps.mtcg.repository.DatabaseCardRepository;

import java.util.Optional;

public class TransactionService {
    private final DatabaseCardRepository databaseCardRepository;
    private DatabasePackageRepository databasePackageRepository;

    public TransactionService(DatabaseCardRepository databaseCardRepository, DatabasePackageRepository databasePackageRepository) {
        this.databaseCardRepository = databaseCardRepository;
        this.databasePackageRepository = databasePackageRepository;
    }

    public Optional<Package> buyPackage(User user, UserService userService) {
        Optional<Package> cardPackage = Optional.empty();
        cardPackage = databasePackageRepository.getPackage();
        if(cardPackage.isPresent()) {
            Package selectedCardPackage = cardPackage.get();
            selectedCardPackage.setCards(this.databaseCardRepository.getCardsFromPackage(selectedCardPackage.getId()));
            this.databaseCardRepository.updateCardsOwner(selectedCardPackage.getCards(), user.getId());
            userService.updateCoins(user.getId(), user.getCoins() - 5);
            this.databasePackageRepository.deletePackage(selectedCardPackage.getId());

            return Optional.of(selectedCardPackage);
        }
        return cardPackage;
    }
}
