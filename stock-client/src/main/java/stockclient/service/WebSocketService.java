package stockclient.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import stockclient.dto.TransactionEventDto;

import java.time.Duration;

/**
 * Service responsible for establishing and maintaining WebSocket connection
 * to the stock-app for receiving transaction events.
 */
@Service
@Slf4j
public class WebSocketService {

    @Value("${stock.app.url:http://localhost:8080}")
    private String stockAppUrl;

    private final TransactionEventService transactionEventService;

    public WebSocketService(TransactionEventService transactionEventService) {
        this.transactionEventService = transactionEventService;
    }

    /**
     * Establishes WebSocket connection to the stock-app
     * and subscribes to transaction events.
     */
    public void connectToWebSocket() {
        log.info("Starting transaction event listener...");
        log.info("Connecting to stock-app at: {}", stockAppUrl);

        WebClient client = WebClient.create();

        client.get()
                .uri(stockAppUrl + "/api/transaction-events/subscribe")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<TransactionEventDto>() {})
                .retry()
                .subscribe(
                        transactionEventService::handleTransactionEvent,
                        this::handleError
                );
    }

    /**
     * Handles errors that occur during the WebSocket connection.
     * 
     * @param error The error that occurred
     */
    private void handleError(Throwable error) {
        log.error("Error receiving transaction events: {}", error.getMessage());
        log.error("Will attempt to reconnect...");
    }
}
