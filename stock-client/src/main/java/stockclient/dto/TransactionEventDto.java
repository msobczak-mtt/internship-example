package stockclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEventDto {
    private Long id;
    private Long clientId;
    private String clientName;
    private Long stockId;
    private String stockSymbol;
    private String type; // BUY, SELL
    private Long quantity;
    private BigDecimal price;
    private BigDecimal totalValue;
    private LocalDateTime transactionDate;
}