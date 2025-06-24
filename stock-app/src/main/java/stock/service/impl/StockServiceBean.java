package stock.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stock.model.Stock;
import stock.repository.StockDao;
import stock.service.StockService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class StockServiceBean implements StockService {

    private final StockDao stockDao;

    @Override
    public List<Stock> findAll() {
        log.info("Finding all stocks...");
        return stockDao.findAll();
    }

    @Override
    public Optional<Stock> findById(Long id) {
        log.info("Finding stock by id: {}", id);
        return stockDao.findById(id);
    }

    @Override
    public Optional<Stock> findBySymbol(String symbol) {
        log.info("Finding stock by symbol: {}", symbol);
        return stockDao.findBySymbol(symbol);
    }

    @Override
    public List<Stock> findByCompanyNameContaining(String companyName) {
        log.info("Finding stocks by company name containing: {}", companyName);
        return stockDao.findByCompanyNameContaining(companyName);
    }

    @Override
    @Transactional
    public Stock createStock(String symbol, String companyName, BigDecimal currentPrice) {
        log.info("Creating stock: {} - {}", symbol, companyName);
        
        // Check if stock with this symbol already exists
        Optional<Stock> existingStock = stockDao.findBySymbol(symbol);
        if (existingStock.isPresent()) {
            throw new IllegalArgumentException("Stock with symbol " + symbol + " already exists");
        }
        
        Stock stock = new Stock(symbol, companyName, currentPrice);
        return stockDao.save(stock);
    }

    @Override
    @Transactional
    public Stock updatePrice(Long id, BigDecimal newPrice) {
        log.info("Updating price for stock id: {} to {}", id, newPrice);
        
        Stock stock = stockDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found with id: " + id));
        
        stock.setCurrentPrice(newPrice);
        stock.setLastUpdate(LocalDateTime.now());
        
        return stockDao.save(stock);
    }

    @Override
    public void updateRecommendationCounts(String symbol, Integer buyCount, Integer sellCount, Integer holdCount) {
        log.info("Updating recommendation counts for {}: BUY={}, SELL={}, HOLD={}", 
                 symbol, buyCount, sellCount, holdCount);
        
        Optional<Stock> stockOpt = stockDao.findBySymbol(symbol);
        if (stockOpt.isPresent()) {
            Stock stock = stockOpt.get();
            stock.setBuyRecommendations(buyCount != null ? buyCount : 0);
            stock.setSellRecommendations(sellCount != null ? sellCount : 0);
            stock.setHoldRecommendations(holdCount != null ? holdCount : 0);
            // Note: @Transient fields don't need to be saved to database
            log.debug("Updated recommendation counts for stock: {}", symbol);
        } else {
            log.warn("Stock with symbol {} not found for recommendation update", symbol);
        }
    }
}