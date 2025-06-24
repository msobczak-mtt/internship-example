package stock.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stock.model.Index;
import stock.service.IndexService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/indices")
@RequiredArgsConstructor
@Slf4j
public class IndexController {

    private final IndexService indexService;

    @GetMapping
    public List<Index> getIndices() {
        log.info("Getting all indices");
        return indexService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Index> getIndex(@PathVariable Long id) {
        log.info("Getting index by id: {}", id);
        Optional<Index> index = indexService.findById(id);
        return ResponseEntity.of(index);
    }

    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<Index> getIndexBySymbol(@PathVariable String symbol) {
        log.info("Getting index by symbol: {}", symbol);
        Optional<Index> index = indexService.findBySymbol(symbol);
        return ResponseEntity.of(index);
    }

    @GetMapping("/stock/{stockId}")
    public List<Index> getIndicesByStock(@PathVariable Long stockId) {
        log.info("Getting indices for stock: {}", stockId);
        return indexService.findIndicesByStockId(stockId);
    }

    @PostMapping
    public Index createIndex(@RequestParam String symbol,
                           @RequestParam String name,
                           @RequestParam String description) {
        log.info("Creating index: {} - {}", symbol, name);
        return indexService.createIndex(symbol, name, description);
    }

    @PutMapping("/{id}/stocks")
    public Index updateIndexComposition(@PathVariable Long id, @RequestBody List<Long> stockIds) {
        log.info("Updating composition for index {}: {} stocks", id, stockIds.size());
        return indexService.updateIndexComposition(id, stockIds);
    }

    @PostMapping("/{indexId}/stocks/{stockId}")
    public Index addStockToIndex(@PathVariable Long indexId, @PathVariable Long stockId) {
        log.info("Adding stock {} to index {}", stockId, indexId);
        return indexService.addStockToIndex(indexId, stockId);
    }

    @DeleteMapping("/{indexId}/stocks/{stockId}")
    public Index removeStockFromIndex(@PathVariable Long indexId, @PathVariable Long stockId) {
        log.info("Removing stock {} from index {}", stockId, indexId);
        return indexService.removeStockFromIndex(indexId, stockId);
    }

    @GetMapping("/{id}/value")
    public BigDecimal calculateIndexValue(@PathVariable Long id) {
        log.info("Calculating value for index: {}", id);
        return indexService.calculateIndexValue(id);
    }
}