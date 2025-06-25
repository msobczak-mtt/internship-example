package stock.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import stock.model.Stock;
import stock.repository.StockRepository;
import stock.service.StockService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class StockServiceBean implements StockService {

    private final StockRepository stockRepository;

    @Override
    public List<Stock> getAllStocks() {
        log.info("Finding all stocks...");
        List<Stock> stocks = stockRepository.findAll();

        // Create a defensive copy of the list to prevent ConcurrentModificationException during serialization
        return new ArrayList<>(stocks);
    }

    @Override
    public Stock getStockById(Long id) {
        log.info("Finding stock by id: {}", id);
        Stock stock = stockRepository.findById(id).orElse(null);
        // No need for additional processing as Stock.getIndices() already returns a synchronized copy
        return stock;
    }

    @Override
    public Stock getStockBySymbol(String symbol) {
        log.info("Finding stock by symbol: {}", symbol);
        Stock stock = stockRepository.findBySymbol(symbol).orElse(null);
        // No need for additional processing as Stock.getIndices() already returns a synchronized copy
        return stock;
    }

    @Override
    public List<Stock> getStocksByCompanyName(String companyName) {
        log.info("Finding stocks by company name containing: {}", companyName);
        List<Stock> stocks = stockRepository.findByCompanyNameContaining(companyName);

        // Create a defensive copy of the list to prevent ConcurrentModificationException during serialization
        return new ArrayList<>(stocks);
    }

    @Override
    public Stock addStock(Stock stock) {
        log.info("Adding stock: {} - {}", stock.getSymbol(), stock.getCompanyName());
        stock.setLastUpdate(LocalDateTime.now());
        Stock savedStock = stockRepository.save(stock);
        // No need for additional processing as Stock.getIndices() already returns a synchronized copy
        return savedStock;
    }

    @Override
    public Stock updatePrice(Long id, BigDecimal newPrice) {
        log.info("Updating price for stock id: {} to {}", id, newPrice);

        Stock stock = stockRepository.findById(id).orElse(null);
        if (stock != null) {
            stock.setCurrentPrice(newPrice);
            stock.setLastUpdate(LocalDateTime.now());
            Stock updatedStock = stockRepository.save(stock);
            // No need for additional processing as Stock.getIndices() already returns a synchronized copy
            return updatedStock;
        }

        throw new RuntimeException("Stock with id " + id + " not found");
    }
}
