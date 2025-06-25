package stock.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude = "indices")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String symbol;          // np. "PKO", "CCC", "KGHM"

    @Column(nullable = false)
    private String companyName;     // pełna nazwa spółki

    @Column(nullable = false)
    private BigDecimal currentPrice;// aktualna cena

    private LocalDateTime lastUpdate;

    @ManyToMany(mappedBy = "stocks", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("stocks")  // Ignoruj pole 'stocks' w Index podczas serializacji
    private Set<Index> indices = Collections.newSetFromMap(new ConcurrentHashMap<>());  // indeksy do których należy akcja

    @Transient
    private Integer buyRecommendations = 0;   // liczba rekomendacji kupna

    @Transient
    private Integer sellRecommendations = 0;  // liczba rekomendacji sprzedaży

    @Transient
    private Integer holdRecommendations = 0;  // liczba rekomendacji trzymaj

    public Stock(String symbol, String companyName, BigDecimal currentPrice) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.currentPrice = currentPrice;
        this.lastUpdate = LocalDateTime.now();
    }

    public void addIndex(Index index) {
        this.indices.add(index);
        index.getStocks().add(this);
    }

    public void removeIndex(Index index) {
        this.indices.remove(index);
        index.getStocks().remove(this);
    }
}
