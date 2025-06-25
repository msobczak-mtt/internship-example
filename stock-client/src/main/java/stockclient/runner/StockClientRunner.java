package stockclient.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import stockclient.service.WebSocketService;

/**
 * CommandLineRunner implementation that starts the WebSocket connection
 * when the application starts.
 */
@Component
@Slf4j
public class StockClientRunner implements CommandLineRunner {

    private final WebSocketService webSocketService;

    public StockClientRunner(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @Override
    public void run(String... args) {
        log.info("Starting Stock Client application...");
        
        // Connect to WebSocket
        webSocketService.connectToWebSocket();
        
        // Keep the application running
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            log.error("Stock Client application interrupted", e);
            Thread.currentThread().interrupt();
        }
    }
}