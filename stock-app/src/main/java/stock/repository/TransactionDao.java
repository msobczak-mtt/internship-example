package stock.repository;

import org.springframework.data.repository.ListCrudRepository;
import stock.model.Client;
import stock.model.Stock;
import stock.model.Transaction;

import java.util.List;

public interface TransactionDao extends ListCrudRepository<Transaction, Long> {
    
    List<Transaction> findByClient(Client client);
    
    List<Transaction> findByStock(Stock stock);
    
    List<Transaction> findByClientOrderByTransactionDateDesc(Client client);
}