package stock.service;

import stock.model.Index;
import stock.model.Stock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IndexService {
    
    List<Index> findAll();
    
    Optional<Index> findById(Long id);
    
    Optional<Index> findBySymbol(String symbol);
    
    List<Index> findIndicesByStock(Stock stock);
    
    List<Index> findIndicesByStockId(Long stockId);
    
    Index createIndex(String symbol, String name, String description);
    
    Index updateIndexComposition(Long indexId, List<Long> stockIds);
    
    Index addStockToIndex(Long indexId, Long stockId);
    
    Index removeStockFromIndex(Long indexId, Long stockId);
    
    BigDecimal calculateIndexValue(Long indexId);
}