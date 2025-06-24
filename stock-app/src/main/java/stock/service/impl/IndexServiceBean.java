package stock.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stock.model.Index;
import stock.model.Stock;
import stock.repository.IndexDao;
import stock.repository.StockDao;
import stock.service.IndexService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class IndexServiceBean implements IndexService {

    private final IndexDao indexDao;
    private final StockDao stockDao;

    @Override
    public List<Index> findAll() {
        log.info("Finding all indices...");
        return indexDao.findAll();
    }

    @Override
    public Optional<Index> findById(Long id) {
        log.info("Finding index by id: {}", id);
        return indexDao.findById(id);
    }

    @Override
    public Optional<Index> findBySymbol(String symbol) {
        log.info("Finding index by symbol: {}", symbol);
        return indexDao.findBySymbol(symbol);
    }

    @Override
    public List<Index> findIndicesByStock(Stock stock) {
        log.info("Finding indices for stock: {}", stock.getSymbol());
        return indexDao.findByStock(stock);
    }

    @Override
    public List<Index> findIndicesByStockId(Long stockId) {
        log.info("Finding indices for stock id: {}", stockId);
        Stock stock = stockDao.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found with id: " + stockId));
        return findIndicesByStock(stock);
    }

    @Override
    @Transactional
    public Index createIndex(String symbol, String name, String description) {
        log.info("Creating index: {} - {}", symbol, name);
        
        // Check if index with this symbol already exists
        Optional<Index> existingIndex = indexDao.findBySymbol(symbol);
        if (existingIndex.isPresent()) {
            throw new IllegalArgumentException("Index with symbol " + symbol + " already exists");
        }
        
        Index index = new Index(symbol, name, description);
        return indexDao.save(index);
    }

    @Override
    @Transactional
    public Index updateIndexComposition(Long indexId, List<Long> stockIds) {
        log.info("Updating composition for index id: {} with {} stocks", indexId, stockIds.size());
        
        Index index = indexDao.findById(indexId)
                .orElseThrow(() -> new IllegalArgumentException("Index not found with id: " + indexId));
        
        // Clear current stocks
        index.getStocks().forEach(stock -> stock.getIndices().remove(index));
        index.getStocks().clear();
        
        // Add new stocks
        for (Long stockId : stockIds) {
            Stock stock = stockDao.findById(stockId)
                    .orElseThrow(() -> new IllegalArgumentException("Stock not found with id: " + stockId));
            index.addStock(stock);
        }
        
        // Recalculate index value
        BigDecimal newValue = calculateIndexValue(indexId);
        index.setCurrentValue(newValue);
        index.setLastUpdate(LocalDateTime.now());
        
        return indexDao.save(index);
    }

    @Override
    @Transactional
    public Index addStockToIndex(Long indexId, Long stockId) {
        log.info("Adding stock {} to index {}", stockId, indexId);
        
        Index index = indexDao.findById(indexId)
                .orElseThrow(() -> new IllegalArgumentException("Index not found with id: " + indexId));
        
        Stock stock = stockDao.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found with id: " + stockId));
        
        // Check if stock is already in index
        if (index.getStocks().contains(stock)) {
            throw new IllegalArgumentException("Stock " + stock.getSymbol() + " is already in index " + index.getSymbol());
        }
        
        index.addStock(stock);
        
        // Recalculate index value
        BigDecimal newValue = calculateIndexValue(indexId);
        index.setCurrentValue(newValue);
        index.setLastUpdate(LocalDateTime.now());
        
        return indexDao.save(index);
    }

    @Override
    @Transactional
    public Index removeStockFromIndex(Long indexId, Long stockId) {
        log.info("Removing stock {} from index {}", stockId, indexId);
        
        Index index = indexDao.findById(indexId)
                .orElseThrow(() -> new IllegalArgumentException("Index not found with id: " + indexId));
        
        Stock stock = stockDao.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found with id: " + stockId));
        
        // Check if stock is in index
        if (!index.getStocks().contains(stock)) {
            throw new IllegalArgumentException("Stock " + stock.getSymbol() + " is not in index " + index.getSymbol());
        }
        
        index.removeStock(stock);
        
        // Recalculate index value
        BigDecimal newValue = calculateIndexValue(indexId);
        index.setCurrentValue(newValue);
        index.setLastUpdate(LocalDateTime.now());
        
        return indexDao.save(index);
    }

    @Override
    public BigDecimal calculateIndexValue(Long indexId) {
        log.debug("Calculating value for index id: {}", indexId);
        
        Index index = indexDao.findById(indexId)
                .orElseThrow(() -> new IllegalArgumentException("Index not found with id: " + indexId));
        
        if (index.getStocks().isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        // Simple calculation: average price of all stocks in the index
        // In real world, this would be weighted by market cap or shares outstanding
        BigDecimal totalValue = index.getStocks().stream()
                .map(Stock::getCurrentPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal indexValue = totalValue.divide(
                BigDecimal.valueOf(index.getStocks().size()), 
                2, 
                RoundingMode.HALF_UP
        );
        
        // Multiply by 100 to get index points (typical for stock indices)
        return indexValue.multiply(BigDecimal.valueOf(100));
    }
}