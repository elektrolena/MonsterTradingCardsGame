package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.TradingDeal;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseCardRepository;
import at.technikum.apps.mtcg.repository.DatabaseTradingRepository;

import java.util.List;
import java.util.Optional;

public class TradingService {
    private final DatabaseCardRepository databaseCardRepository;
    private final DatabaseTradingRepository databaseTradingRepository;
    public TradingService(DatabaseCardRepository databaseCardRepository, DatabaseTradingRepository databaseTradingRepository) {
        this.databaseCardRepository = databaseCardRepository;
        this.databaseTradingRepository = databaseTradingRepository;
    }

    public Optional<List<TradingDeal>> getAllTradingDeals() {
        return this.databaseTradingRepository.getAllTradingDeals();
    }
    public int openTradingDeal(TradingDeal tradingDeal, User user) {
        if(this.databaseCardRepository.checkForOwnership(tradingDeal.getCardId(), user.getId()).isEmpty() || this.databaseCardRepository.isInDeck(tradingDeal.getCardId())) {
            return 403;
        }
        if(this.databaseTradingRepository.doesTradingDealExist(tradingDeal.getId())) {
            return 409;
        }
        tradingDeal.setUserId(user.getId());
        this.databaseTradingRepository.saveTradingDeal(tradingDeal);

        return 201;
    }
}
