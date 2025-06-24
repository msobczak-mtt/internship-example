package stock.service;

import stock.model.Client;
import stock.model.Stock;
import stock.model.Transaction;
import stock.model.TransactionType;

import java.util.List;

public interface TransactionService {
    
    List<Transaction> findAll();
    
    List<Transaction> findByClient(Client client);
    
    List<Transaction> findByClientId(Long clientId);
    
    Transaction executeTransaction(Long clientId, Long stockId, TransactionType type, Long quantity);
    
    void validateTransaction(Long clientId, Long stockId, TransactionType type, Long quantity);
}