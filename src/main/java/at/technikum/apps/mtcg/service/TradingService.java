package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.TradingDeal;
import at.technikum.apps.mtcg.repository.DatabaseTradingRepository;

import java.util.List;
import java.util.Optional;

public class TradingService {
    private final DatabaseTradingRepository databaseTradingRepository;
    public TradingService(DatabaseTradingRepository databaseTradingRepository) {
        this.databaseTradingRepository = databaseTradingRepository;
    }

    public Optional<List<TradingDeal>> getAllTradingDeals() {
        return this.databaseTradingRepository.getAllTradingDeals();
    }
    public int createTradingDeal() {
        return 0;
    }
}
