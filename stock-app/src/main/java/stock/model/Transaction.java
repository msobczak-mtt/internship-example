package stock.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    
    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;
    
    @Enumerated(EnumType.STRING)
    private TransactionType type;    // BUY, SELL
    
    @Column(nullable = false)
    private Long quantity;
    
    @Column(nullable = false)
    private BigDecimal price;        // cena w momencie transakcji
    
    private LocalDateTime transactionDate;

    public Transaction(Client client, Stock stock, TransactionType type, Long quantity, BigDecimal price) {
        this.client = client;
        this.stock = stock;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.transactionDate = LocalDateTime.now();
    }

    public BigDecimal getTotalValue() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}