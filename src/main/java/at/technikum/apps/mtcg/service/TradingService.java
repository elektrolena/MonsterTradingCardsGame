package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.TradingDeal;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.exceptions.ExceptionMessage;
import at.technikum.apps.mtcg.exceptions.HttpStatusException;
import at.technikum.apps.mtcg.repository.DatabaseCardRepository;
import at.technikum.apps.mtcg.repository.DatabaseTradingRepository;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TradingService {
    private final AuthorizationService authorizationService;
    private final DatabaseCardRepository databaseCardRepository;
    private final DatabaseTradingRepository databaseTradingRepository;
    public TradingService(AuthorizationService authorizationService, DatabaseCardRepository databaseCardRepository, DatabaseTradingRepository databaseTradingRepository) {
        this.authorizationService = authorizationService;
        this.databaseCardRepository = databaseCardRepository;
        this.databaseTradingRepository = databaseTradingRepository;
    }

    public List<TradingDeal> getAllTradingDeals(String authorizationToken) {
        this.authorizationService.getLoggedInUser(authorizationToken);
        List<TradingDeal> tradingDeals = this.databaseTradingRepository.getAllTradingDeals();
        if(tradingDeals.isEmpty()) {
            throw new HttpStatusException(HttpStatus.NO_CONTENT, HttpContentType.TEXT_PLAIN, ExceptionMessage.NO_CONTENT_TRADING);
        }
        return tradingDeals;
    }
    public void openTradingDeal(String authorizationToken, TradingDeal tradingDeal) {
        User user = this.authorizationService.getLoggedInUser(authorizationToken);

        if(this.databaseCardRepository.checkForOwnership(tradingDeal.getCardId(), user.getId()).isEmpty() || this.databaseCardRepository.isInDeck(tradingDeal.getCardId())) {
            throw new HttpStatusException(HttpStatus.FORBIDDEN, HttpContentType.TEXT_PLAIN, ExceptionMessage.FORBIDDEN_TRADING_OPEN);
        }

        if(this.databaseTradingRepository.doesTradingDealExist(tradingDeal.getId())) {
            throw new HttpStatusException(HttpStatus.ALREADY_EXISTS, HttpContentType.TEXT_PLAIN, ExceptionMessage.ALREADY_EXISTS_TRADING);
        }

        tradingDeal.setUserId(user.getId());
        this.databaseTradingRepository.saveTradingDeal(tradingDeal);
    }

    public void deleteOpenTradingDeal(String authorizationToken, String tradingDealId) {
        User user = this.authorizationService.getLoggedInUser(authorizationToken);

        Optional<TradingDeal> foundDeal = this.databaseTradingRepository.getTradingDeal(tradingDealId);
        if(foundDeal.isEmpty()) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, HttpContentType.TEXT_PLAIN, ExceptionMessage.NOT_FOUND_TRADING);
        }

        TradingDeal tradingDeal = foundDeal.get();
        if(!Objects.equals(tradingDeal.getUserId(), user.getId())) {
            throw new HttpStatusException(HttpStatus.FORBIDDEN, HttpContentType.TEXT_PLAIN, ExceptionMessage.FORBIDDEN_TRADING_DELETE);
        }

        this.databaseTradingRepository.deleteTradingDeal(tradingDealId);
    }

    public void finishTradingDeal(String authorizationToken, Card offeredCard, String tradingDealId) {
        User offeringUser = this.authorizationService.getLoggedInUser(authorizationToken);

        Optional<TradingDeal> foundDeal = this.databaseTradingRepository.getTradingDeal(tradingDealId);
        if(foundDeal.isEmpty()) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, HttpContentType.TEXT_PLAIN, ExceptionMessage.NOT_FOUND_TRADING);
        }

        TradingDeal tradingDeal = foundDeal.get();
        if(this.databaseCardRepository.checkForOwnership(offeredCard.getId(), offeringUser.getId()).isEmpty() || this.databaseCardRepository.isInDeck(offeredCard.getId()) || Objects.equals(offeringUser.getId(), tradingDeal.getUserId()) || !Objects.equals(offeredCard.getType(), tradingDeal.getDesiredType()) || !Objects.equals(offeredCard.getDamage(), tradingDeal.getDesiredDamage())) {
            throw new HttpStatusException(HttpStatus.FORBIDDEN, HttpContentType.TEXT_PLAIN, ExceptionMessage.FORBIDDEN_TRADING_EXECUTE);
        }

        this.databaseCardRepository.updateCardOwner(offeredCard.getId(), tradingDeal.getUserId());
        this.databaseCardRepository.updateCardOwner(tradingDeal.getCardId(), offeringUser.getId());
        this.databaseTradingRepository.deleteTradingDeal(tradingDealId);
    }
}
