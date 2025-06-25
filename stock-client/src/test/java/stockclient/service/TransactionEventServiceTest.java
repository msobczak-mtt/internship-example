package stockclient.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import stockclient.dto.TransactionEventDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
public class TransactionEventServiceTest {

    @InjectMocks
    private TransactionEventService transactionEventService;

    @Test
    public void testHandleTransactionEvent() {
        // Create a sample transaction event
        TransactionEventDto event = new TransactionEventDto();
        event.setId(123L);
        event.setClientId(1L);
        event.setClientName("John Doe");
        event.setStockId(2L);
        event.setStockSymbol("AAPL");
        event.setType("BUY");
        event.setQuantity(10L);
        event.setPrice(BigDecimal.valueOf(150));
        event.setTotalValue(BigDecimal.valueOf(1500));
        event.setTransactionDate(LocalDateTime.now());

        // Test that the method doesn't throw an exception
        assertDoesNotThrow(() -> {
            ReflectionTestUtils.invokeMethod(transactionEventService, "handleTransactionEvent", event);
        });
    }

    @Test
    public void testHandleError() {
        // Test that the method doesn't throw an exception
        assertDoesNotThrow(() -> {
            ReflectionTestUtils.invokeMethod(transactionEventService, "handleError", new RuntimeException("Test error"));
        });
    }
}