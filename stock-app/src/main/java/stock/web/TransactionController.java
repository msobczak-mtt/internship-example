package stock.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import stock.model.Transaction;
import stock.model.TransactionType;
import stock.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public List<Transaction> getTransactions() {
        log.info("Getting all transactions");
        return transactionService.findAll();
    }

    @GetMapping("/client/{clientId}")
    public List<Transaction> getTransactionsByClient(@PathVariable Long clientId) {
        log.info("Getting transactions for client: {}", clientId);
        return transactionService.findByClientId(clientId);
    }

    @PostMapping
    public Transaction executeTransaction(@RequestParam Long clientId,
                                        @RequestParam Long stockId,
                                        @RequestParam TransactionType type,
                                        @RequestParam Long quantity) {
        log.info("Executing transaction: client={}, stock={}, type={}, quantity={}", 
                 clientId, stockId, type, quantity);
        return transactionService.executeTransaction(clientId, stockId, type, quantity);
    }
}