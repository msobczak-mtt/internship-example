package stock.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stock.model.Stock;
import stock.service.StockService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
@Slf4j
public class StockController {

    private final StockService stockService;

    @GetMapping
    public List<Stock> getStocks() {
        log.info("Getting all stocks");
        return stockService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStock(@PathVariable Long id) {
        log.info("Getting stock by id: {}", id);
        Optional<Stock> stock = stockService.findById(id);
        return ResponseEntity.of(stock);
    }

    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<Stock> getStockBySymbol(@PathVariable String symbol) {
        log.info("Getting stock by symbol: {}", symbol);
        Optional<Stock> stock = stockService.findBySymbol(symbol);
        return ResponseEntity.of(stock);
    }

    @GetMapping("/search")
    public List<Stock> searchStocks(@RequestParam String companyName) {
        log.info("Searching stocks by company name: {}", companyName);
        return stockService.findByCompanyNameContaining(companyName);
    }

    @PostMapping
    public Stock createStock(@RequestParam String symbol, 
                           @RequestParam String companyName, 
                           @RequestParam BigDecimal currentPrice) {
        log.info("Creating stock: {} - {}", symbol, companyName);
        return stockService.createStock(symbol, companyName, currentPrice);
    }

    @PutMapping("/{id}/price")
    public Stock updatePrice(@PathVariable Long id, @RequestParam BigDecimal newPrice) {
        log.info("Updating price for stock {}: {}", id, newPrice);
        return stockService.updatePrice(id, newPrice);
    }
}