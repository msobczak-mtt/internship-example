package stock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import stock.model.Transaction;
import stock.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEventDto {
    private Long id;
    private Long clientId;
    private String clientName;
    private Long stockId;
    private String stockSymbol;
    private TransactionType type;
    private Long quantity;
    private BigDecimal price;
    private BigDecimal totalValue;
    private LocalDateTime transactionDate;
    
    public static TransactionEventDto fromTransaction(Transaction transaction) {
        return TransactionEventDto.builder()
                .id(transaction.getId())
                .clientId(transaction.getClient().getId())
                .clientName(transaction.getClient().getFirstName() + " " + transaction.getClient().getLastName())
                .stockId(transaction.getStock().getId())
                .stockSymbol(transaction.getStock().getSymbol())
                .type(transaction.getType())
                .quantity(transaction.getQuantity())
                .price(transaction.getPrice())
                .totalValue(transaction.getTotalValue())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }
}