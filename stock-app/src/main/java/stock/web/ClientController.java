package stock.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stock.model.Client;
import stock.service.ClientService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public List<Client> getClients() {
        log.info("Getting all clients");
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClient(@PathVariable Long id) {
        log.info("Getting client by id: {}", id);
        Optional<Client> client = clientService.findById(id);
        return ResponseEntity.of(client);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Client> getClientByEmail(@PathVariable String email) {
        log.info("Getting client by email: {}", email);
        Optional<Client> client = clientService.findByEmail(email);
        return ResponseEntity.of(client);
    }

    @PostMapping
    public Client createClient(@RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam String email,
                             @RequestParam BigDecimal balance) {
        log.info("Creating client: {} {} - {}", firstName, lastName, email);
        return clientService.createClient(firstName, lastName, email, balance);
    }

    @PutMapping("/{id}/balance")
    public Client updateBalance(@PathVariable Long id, @RequestParam BigDecimal newBalance) {
        log.info("Updating balance for client {}: {}", id, newBalance);
        return clientService.updateBalance(id, newBalance);
    }
}