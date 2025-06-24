package stock.service;

import stock.model.Stock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface StockService {
    
    List<Stock> findAll();
    
    Optional<Stock> findById(Long id);
    
    Optional<Stock> findBySymbol(String symbol);
    
    List<Stock> findByCompanyNameContaining(String companyName);
    
    Stock createStock(String symbol, String companyName, BigDecimal currentPrice);
    
    Stock updatePrice(Long id, BigDecimal newPrice);
    
    void updateRecommendationCounts(String symbol, Integer buyCount, Integer sellCount, Integer holdCount);
}