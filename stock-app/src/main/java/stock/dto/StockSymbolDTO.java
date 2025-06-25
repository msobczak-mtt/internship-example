package stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing just the symbol of a stock for use in index composition.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockSymbolDTO {
    private Long id;
    private String symbol;
}