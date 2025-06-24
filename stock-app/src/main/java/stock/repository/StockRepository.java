package stock.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import stock.model.Stock;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends ListCrudRepository<Stock, Long> {

    Optional<Stock> findBySymbol(String symbol);
    
    List<Stock> findByCompanyNameContaining(String companyName);
    
    @Query("SELECT s FROM Stock s WHERE s.currentPrice > :price")
    List<Stock> findByPriceGreaterThan(@Param("price") java.math.BigDecimal price);
}