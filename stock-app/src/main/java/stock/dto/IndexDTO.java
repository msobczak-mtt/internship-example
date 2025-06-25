package stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import stock.model.Index;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO representing an Index with its composition as a list of stock symbols.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndexDTO {
    private Long id;
    private String symbol;
    private String name;
    private String description;
    private BigDecimal currentValue;
    private LocalDateTime lastUpdate;
    private List<StockSymbolDTO> stockSymbols;

    /**
     * Converts an Index entity to an IndexDTO.
     *
     * @param index the Index entity to convert
     * @return the corresponding IndexDTO
     */
    public static IndexDTO fromEntity(Index index) {
        if (index == null) {
            return null;
        }

        IndexDTO dto = new IndexDTO();
        dto.setId(index.getId());
        dto.setSymbol(index.getSymbol());
        dto.setName(index.getName());
        dto.setDescription(index.getDescription());
        dto.setCurrentValue(index.getCurrentValue());
        dto.setLastUpdate(index.getLastUpdate());
        
        // Convert the stocks to StockSymbolDTOs
        dto.setStockSymbols(index.getStocks().stream()
                .map(stock -> new StockSymbolDTO(stock.getId(), stock.getSymbol()))
                .collect(Collectors.toList()));
        
        return dto;
    }
}