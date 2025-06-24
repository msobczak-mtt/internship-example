package stock.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import stock.model.Client;
import stock.model.Stock;
import stock.model.Transaction;

import java.util.List;

public interface TransactionRepository extends ListCrudRepository<Transaction, Long> {

    List<Transaction> findByClient(Client client);
    
    List<Transaction> findByStock(Stock stock);
    
    @Query("SELECT t FROM Transaction t WHERE t.client = :client ORDER BY t.transactionDate DESC")
    List<Transaction> findByClientOrderByTransactionDateDesc(@Param("client") Client client);
}