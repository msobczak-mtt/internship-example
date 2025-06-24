package stock.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "stock_index")  // "index" jest słowem kluczowym w SQL
@Data
@NoArgsConstructor
@ToString(exclude = "stocks")
public class Index {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String symbol;           // np. "WIG20", "mWIG40", "sWIG80"
    
    @Column(nullable = false)
    private String name;             // pełna nazwa indeksu
    
    private String description;      // opis indeksu
    
    @ManyToMany
    @JoinTable(
        name = "index_stocks",
        joinColumns = @JoinColumn(name = "index_id"),
        inverseJoinColumns = @JoinColumn(name = "stock_id")
    )
    private Set<Stock> stocks = new HashSet<>();  // akcje w indeksie
    
    private BigDecimal currentValue; // aktualna wartość indeksu
    
    private LocalDateTime lastUpdate;

    public Index(String symbol, String name, String description) {
        this.symbol = symbol;
        this.name = name;
        this.description = description;
        this.currentValue = BigDecimal.ZERO;
        this.lastUpdate = LocalDateTime.now();
    }

    public void addStock(Stock stock) {
        this.stocks.add(stock);
        stock.getIndices().add(this);
    }

    public void removeStock(Stock stock) {
        this.stocks.remove(stock);
        stock.getIndices().remove(this);
    }
}