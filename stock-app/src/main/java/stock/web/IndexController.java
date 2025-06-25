package stock.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stock.dto.IndexDTO;
import stock.model.Index;
import stock.service.IndexService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/indices")
@RequiredArgsConstructor
@Slf4j
public class IndexController {

    private final IndexService indexService;

    @GetMapping
    public List<IndexDTO> getIndices() {
        log.info("Getting all indices");
        List<Index> indices = indexService.findAll();
        return indices.stream()
                .map(IndexDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IndexDTO> getIndex(@PathVariable Long id) {
        log.info("Getting index by id: {}", id);
        Optional<Index> index = indexService.findById(id);
        return index.map(i -> ResponseEntity.ok(IndexDTO.fromEntity(i)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<IndexDTO> getIndexBySymbol(@PathVariable String symbol) {
        log.info("Getting index by symbol: {}", symbol);
        Optional<Index> index = indexService.findBySymbol(symbol);
        return index.map(i -> ResponseEntity.ok(IndexDTO.fromEntity(i)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/stock/{stockId}")
    public List<IndexDTO> getIndicesByStock(@PathVariable Long stockId) {
        log.info("Getting indices for stock: {}", stockId);
        List<Index> indices = indexService.findIndicesByStockId(stockId);
        return indices.stream()
                .map(IndexDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @PostMapping
    public IndexDTO createIndex(@RequestParam String symbol,
                           @RequestParam String name,
                           @RequestParam String description) {
        log.info("Creating index: {} - {}", symbol, name);
        Index index = indexService.createIndex(symbol, name, description);
        return IndexDTO.fromEntity(index);
    }

    @PutMapping("/{id}/stocks")
    public IndexDTO updateIndexComposition(@PathVariable Long id, @RequestBody List<Long> stockIds) {
        log.info("Updating composition for index {}: {} stocks", id, stockIds.size());
        Index index = indexService.updateIndexComposition(id, stockIds);
        return IndexDTO.fromEntity(index);
    }

    @PostMapping("/{indexId}/stocks/{stockId}")
    public IndexDTO addStockToIndex(@PathVariable Long indexId, @PathVariable Long stockId) {
        log.info("Adding stock {} to index {}", stockId, indexId);
        Index index = indexService.addStockToIndex(indexId, stockId);
        return IndexDTO.fromEntity(index);
    }

    @DeleteMapping("/{indexId}/stocks/{stockId}")
    public IndexDTO removeStockFromIndex(@PathVariable Long indexId, @PathVariable Long stockId) {
        log.info("Removing stock {} from index {}", stockId, indexId);
        Index index = indexService.removeStockFromIndex(indexId, stockId);
        return IndexDTO.fromEntity(index);
    }

    @GetMapping("/{id}/value")
    public BigDecimal calculateIndexValue(@PathVariable Long id) {
        log.info("Calculating value for index: {}", id);
        return indexService.calculateIndexValue(id);
    }
}
