package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.TradingDeal;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.repository.DatabaseCardRepository;
import at.technikum.apps.mtcg.repository.DatabaseTradingRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
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

    public int deleteOpenTradingDeal(String tradingDealId, User user) {
        Optional<TradingDeal> foundDeal = this.databaseTradingRepository.getTradingDeal(tradingDealId);
        if(foundDeal.isEmpty()) {
            return 404;
        }
        TradingDeal tradingDeal = foundDeal.get();
        if(!Objects.equals(tradingDeal.getUserId(), user.getId())) {
            return 403;
        }
        this.databaseTradingRepository.deleteTradingDeal(tradingDealId);
        return 200;
    }

    public int finishTradingDeal(Card offeredCard, User offeringUser, String tradingDealId) {
        Optional<TradingDeal> foundDeal = this.databaseTradingRepository.getTradingDeal(tradingDealId);
        if(foundDeal.isEmpty()) {
            return 404;
        }
        TradingDeal tradingDeal = foundDeal.get();
        if(this.databaseCardRepository.checkForOwnership(offeredCard.getId(), offeringUser.getId()).isEmpty() || this.databaseCardRepository.isInDeck(offeredCard.getId()) || Objects.equals(offeringUser.getId(), tradingDeal.getUserId()) || !Objects.equals(offeredCard.getType(), tradingDeal.getDesiredType()) || !Objects.equals(offeredCard.getDamage(), tradingDeal.getDesiredDamage())) {
            return 403;
        }
        this.databaseCardRepository.updateCardOwner(offeredCard.getId(), tradingDeal.getUserId());
        this.databaseCardRepository.updateCardOwner(tradingDeal.getCardId(), offeringUser.getId());
        this.databaseTradingRepository.deleteTradingDeal(tradingDealId);
        return 200;
    }
}
