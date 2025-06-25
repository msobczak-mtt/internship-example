package stock.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stock.model.Client;
import stock.model.Stock;
import stock.model.Transaction;
import stock.model.TransactionType;
import stock.repository.ClientDao;
import stock.repository.StockDao;
import stock.repository.TransactionDao;
import stock.service.TransactionService;
import stock.web.TransactionEventController;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class TransactionServiceBean implements TransactionService {

    private final TransactionDao transactionDao;
    private final ClientDao clientDao;
    private final StockDao stockDao;
    private final TransactionEventController transactionEventController;

    @Override
    public List<Transaction> findAll() {
        log.info("Finding all transactions...");
        return transactionDao.findAll();
    }

    @Override
    public List<Transaction> findByClient(Client client) {
        log.info("Finding transactions for client: {}", client.getId());
        return transactionDao.findByClientOrderByTransactionDateDesc(client);
    }

    @Override
    public List<Transaction> findByClientId(Long clientId) {
        log.info("Finding transactions for client id: {}", clientId);
        Client client = clientDao.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + clientId));
        return findByClient(client);
    }

    @Override
    @Transactional
    public Transaction executeTransaction(Long clientId, Long stockId, TransactionType type, Long quantity) {
        log.info("Executing transaction: client={}, stock={}, type={}, quantity={}", 
                 clientId, stockId, type, quantity);

        // Validate transaction first
        validateTransaction(clientId, stockId, type, quantity);

        Client client = clientDao.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + clientId));

        Stock stock = stockDao.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found with id: " + stockId));

        BigDecimal transactionValue = stock.getCurrentPrice().multiply(BigDecimal.valueOf(quantity));

        // Update client balance
        if (type == TransactionType.BUY) {
            client.setBalance(client.getBalance().subtract(transactionValue));
        } else if (type == TransactionType.SELL) {
            client.setBalance(client.getBalance().add(transactionValue));
        }

        clientDao.save(client);

        // Create and save transaction
        Transaction transaction = new Transaction(client, stock, type, quantity, stock.getCurrentPrice());
        transaction = transactionDao.save(transaction);

        // Send transaction event
        log.info("Sending transaction event for transaction id: {}", transaction.getId());
        transactionEventController.sendTransactionEvent(transaction);

        return transaction;
    }

    @Override
    public void validateTransaction(Long clientId, Long stockId, TransactionType type, Long quantity) {
        log.debug("Validating transaction: client={}, stock={}, type={}, quantity={}", 
                 clientId, stockId, type, quantity);

        // Validate quantity
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        // Validate client exists
        Client client = clientDao.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + clientId));

        // Validate stock exists
        Stock stock = stockDao.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found with id: " + stockId));

        // Calculate transaction value
        BigDecimal transactionValue = stock.getCurrentPrice().multiply(BigDecimal.valueOf(quantity));

        // Validate client has sufficient funds for BUY transactions
        if (type == TransactionType.BUY) {
            if (client.getBalance().compareTo(transactionValue) < 0) {
                throw new IllegalArgumentException(
                    String.format("Insufficient funds. Required: %s, Available: %s", 
                                transactionValue, client.getBalance()));
            }
        }

        // For SELL transactions, we could validate if client owns enough stocks
        // but this would require tracking stock ownership, which is not in our current model
        // For simplicity, we'll allow all SELL transactions

        log.debug("Transaction validation successful");
    }
}
