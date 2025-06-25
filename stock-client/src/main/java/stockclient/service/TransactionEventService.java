package stockclient.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import stockclient.dto.TransactionEventDto;

/**
 * Service responsible for handling transaction events received from the WebSocket connection.
 */
@Service
@Slf4j
public class TransactionEventService {

    /**
     * Handles a transaction event received from the WebSocket connection.
     * 
     * @param event The transaction event to handle
     */
    public void handleTransactionEvent(TransactionEventDto event) {
        log.info("=== New Transaction Event ===");
        log.info("Transaction ID: {}", event.getId());
        log.info("Client: {} (ID: {})", event.getClientName(), event.getClientId());
        log.info("Stock: {} (ID: {})", event.getStockSymbol(), event.getStockId());
        log.info("Type: {}", event.getType());
        log.info("Quantity: {}", event.getQuantity());
        log.info("Price: {}", event.getPrice());
        log.info("Total Value: {}", event.getTotalValue());
        log.info("Date: {}", event.getTransactionDate());
        log.info("============================");
    }
}
