package stock.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import stock.model.Index;
import stock.model.Stock;

import java.util.List;
import java.util.Optional;

public interface IndexRepository extends ListCrudRepository<Index, Long> {

    Optional<Index> findBySymbol(String symbol);
    
    @Query("SELECT i FROM Index i JOIN i.stocks s WHERE s = :stock")
    List<Index> findByStock(@Param("stock") Stock stock);
}