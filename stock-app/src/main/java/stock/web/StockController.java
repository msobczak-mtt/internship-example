package stock.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stock.model.Stock;
import stock.service.StockService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
@Slf4j
public class StockController {

    private final StockService stockService;

    // Prosty sposób - bezpośredni zwrot obiektu
    @GetMapping
    public List<Stock> getAllStocks() {
        log.info("Getting all stocks");
        List<Stock> allStocks = stockService.getAllStocks();
        if (allStocks.isEmpty()) {
            log.warn("No stocks found");
        } else {
            log.info("Found {} stocks", allStocks.size());
        }
        return allStocks;
    }

    // Przykład z ResponseEntity - lepsze dla obsługi błędów
    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStock(@PathVariable Long id) {
        log.info("Getting stock by id: {}", id);
        Stock stock = stockService.getStockById(id);
        
        if (stock != null) {
            return ResponseEntity.ok(stock);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Prosty sposób
    @GetMapping("/symbol/{symbol}")
    public Stock getStockBySymbol(@PathVariable String symbol) {
        log.info("Getting stock by symbol: {}", symbol);
        return stockService.getStockBySymbol(symbol);
    }

    // Prosty sposób
    @GetMapping("/search")
    public List<Stock> searchStocks(@RequestParam String companyName) {
        log.info("Searching stocks by company name: {}", companyName);
        return stockService.getStocksByCompanyName(companyName);
    }

    // Przykład z ResponseEntity - zwracamy kod 201 Created
    @PostMapping
    public ResponseEntity<Stock> addStock(@RequestBody Stock stock) {
        log.info("Adding stock: {} - {}", stock.getSymbol(), stock.getCompanyName());
        try {
            Stock savedStock = stockService.addStock(stock);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStock);
        } catch (Exception e) {
            log.error("Error adding stock: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // Prosty sposób
    @PutMapping("/{id}/price")
    public Stock updatePrice(@PathVariable Long id, @RequestParam BigDecimal newPrice) {
        log.info("Updating price for stock {}: {}", id, newPrice);
        return stockService.updatePrice(id, newPrice);
    }

    // Dodatkowy przykład z ResponseEntity - DELETE z właściwym kodem odpowiedzi
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        log.info("Deleting stock with id: {}", id);
        Stock stock = stockService.getStockById(id);
        
        if (stock != null) {
            // Tu byłaby logika usuwania - na razie tylko przykład
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}