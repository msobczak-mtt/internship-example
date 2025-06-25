package stock.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "stock_index")  // "index" jest słowem kluczowym w SQL
@Data
@NoArgsConstructor
@ToString(exclude = "stocks")
@lombok.EqualsAndHashCode(exclude = {"stocks"})
public class Index {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String symbol;           // np. "WIG20", "mWIG40", "sWIG80"

    @Column(nullable = false)
    private String name;             // pełna nazwa indeksu

    private String description;      // opis indeksu

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "index_stocks",
        joinColumns = @JoinColumn(name = "index_id"),
        inverseJoinColumns = @JoinColumn(name = "stock_id")
    )
    @Getter(AccessLevel.NONE)
    private Set<Stock> stocks = Collections.newSetFromMap(new ConcurrentHashMap<>());  // akcje w indeksie

    public Set<Stock> getStocks() {
        synchronized(stocks) {
            return new HashSet<>(stocks);
        }
    }

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
