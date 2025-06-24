package stock.repository;

import org.springframework.data.repository.ListCrudRepository;
import stock.model.Stock;

import java.util.List;
import java.util.Optional;

public interface StockDao extends ListCrudRepository<Stock, Long> {
    
    Optional<Stock> findBySymbol(String symbol);
    
    List<Stock> findByCompanyNameContaining(String companyName);
}