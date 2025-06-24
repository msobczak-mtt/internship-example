package stock.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stock.model.Client;
import stock.repository.ClientDao;
import stock.service.ClientService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ClientServiceBean implements ClientService {

    private final ClientDao clientDao;

    @Override
    public List<Client> findAll() {
        log.info("Finding all clients...");
        return clientDao.findAll();
    }

    @Override
    public Optional<Client> findById(Long id) {
        log.info("Finding client by id: {}", id);
        return clientDao.findById(id);
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        log.info("Finding client by email: {}", email);
        return clientDao.findByEmail(email);
    }

    @Override
    @Transactional
    public Client createClient(String firstName, String lastName, String email, BigDecimal balance) {
        log.info("Creating client: {} {} - {}", firstName, lastName, email);
        
        // Check if client with this email already exists
        Optional<Client> existingClient = clientDao.findByEmail(email);
        if (existingClient.isPresent()) {
            throw new IllegalArgumentException("Client with email " + email + " already exists");
        }
        
        // Validate balance
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        
        Client client = new Client(firstName, lastName, email, balance);
        return clientDao.save(client);
    }

    @Override
    @Transactional
    public Client updateBalance(Long id, BigDecimal newBalance) {
        log.info("Updating balance for client id: {} to {}", id, newBalance);
        
        Client client = clientDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + id));
        
        // Validate new balance
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        
        client.setBalance(newBalance);
        
        return clientDao.save(client);
    }
}