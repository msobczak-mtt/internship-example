package stock.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import stock.model.Client;
import stock.model.Stock;
import stock.model.Transaction;
import stock.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(TransactionEventController.class)
public class TransactionEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionEventController controller;

    @Test
    public void testSubscribe() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/transaction-events/subscribe")
                .accept(MediaType.TEXT_EVENT_STREAM_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_EVENT_STREAM_VALUE))
                .andReturn();

        // Verify that the connection is established
        // Note: We can't easily test the actual event sending in a unit test
    }

    @Test
    public void testSendTransactionEvent() {
        // Create a mock transaction
        Client client = new Client();
        client.setId(1L);
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setBalance(BigDecimal.valueOf(1000));

        Stock stock = new Stock();
        stock.setId(2L);
        stock.setSymbol("AAPL");
        stock.setCompanyName("Apple Inc.");
        stock.setCurrentPrice(BigDecimal.valueOf(150));

        Transaction transaction = new Transaction(client, stock, TransactionType.BUY, 10L, BigDecimal.valueOf(150));
        transaction.setId(123L);
        transaction.setTransactionDate(LocalDateTime.now());

        // This just verifies that the method doesn't throw an exception
        // In a real scenario, we would need integration tests to verify the events are received
        controller.sendTransactionEvent(transaction);
    }
}
