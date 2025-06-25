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
import java.util.ArrayList;
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
        List<Index> indices = indexDao.findAll();

        // Create a defensive copy of the list to prevent ConcurrentModificationException during serialization
        return new ArrayList<>(indices);
    }

    @Override
    public Optional<Index> findById(Long id) {
        log.info("Finding index by id: {}", id);
        // No need for additional processing as Index.getStocks() already returns a synchronized copy
        return indexDao.findById(id);
    }

    @Override
    public Optional<Index> findBySymbol(String symbol) {
        log.info("Finding index by symbol: {}", symbol);
        // No need for additional processing as Index.getStocks() already returns a synchronized copy
        return indexDao.findBySymbol(symbol);
    }

    @Override
    public List<Index> findIndicesByStock(Stock stock) {
        log.info("Finding indices for stock: {}", stock.getSymbol());
        List<Index> indices = indexDao.findByStock(stock);

        // Create a defensive copy of the list to prevent ConcurrentModificationException during serialization
        return new ArrayList<>(indices);
    }

    @Override
    public List<Index> findIndicesByStockId(Long stockId) {
        log.info("Finding indices for stock id: {}", stockId);
        Stock stock = stockDao.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found with id: " + stockId));
        // No need for additional defensive copy as findIndicesByStock already returns a defensive copy
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
        Index savedIndex = indexDao.save(index);
        // No need for additional processing as Index.getStocks() already returns a synchronized copy
        return savedIndex;
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

        Index updatedIndex = indexDao.save(index);
        // No need for additional processing as Index.getStocks() already returns a synchronized copy
        return updatedIndex;
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

        Index updatedIndex = indexDao.save(index);
        // No need for additional processing as Index.getStocks() already returns a synchronized copy
        return updatedIndex;
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

        Index updatedIndex = indexDao.save(index);
        // No need for additional processing as Index.getStocks() already returns a synchronized copy
        return updatedIndex;
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
