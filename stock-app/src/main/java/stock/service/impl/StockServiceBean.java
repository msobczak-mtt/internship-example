package stock.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import stock.model.Stock;
import stock.repository.StockRepository;
import stock.service.StockService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class StockServiceBean implements StockService {

    private final StockRepository stockRepository;

    @Override
    public List<Stock> getAllStocks() {
        log.info("Finding all stocks...");
        return stockRepository.findAll();
    }

    @Override
    public Stock getStockById(Long id) {
        log.info("Finding stock by id: {}", id);
        return stockRepository.findById(id).orElse(null);
    }

    @Override
    public Stock getStockBySymbol(String symbol) {
        log.info("Finding stock by symbol: {}", symbol);
        return stockRepository.findBySymbol(symbol).orElse(null);
    }

    @Override
    public List<Stock> getStocksByCompanyName(String companyName) {
        log.info("Finding stocks by company name containing: {}", companyName);
        return stockRepository.findByCompanyNameContaining(companyName);
    }

    @Override
    public Stock addStock(Stock stock) {
        log.info("Adding stock: {} - {}", stock.getSymbol(), stock.getCompanyName());
        stock.setLastUpdate(LocalDateTime.now());
        return stockRepository.save(stock);
    }

    @Override
    public Stock updatePrice(Long id, BigDecimal newPrice) {
        log.info("Updating price for stock id: {} to {}", id, newPrice);
        
        Stock stock = stockRepository.findById(id).orElse(null);
        if (stock != null) {
            stock.setCurrentPrice(newPrice);
            stock.setLastUpdate(LocalDateTime.now());
            return stockRepository.save(stock);
        }
        
        throw new RuntimeException("Stock with id " + id + " not found");
    }
}