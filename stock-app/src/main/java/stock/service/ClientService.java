package stock.service;

import stock.model.Client;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ClientService {
    
    List<Client> findAll();
    
    Optional<Client> findById(Long id);
    
    Optional<Client> findByEmail(String email);
    
    Client createClient(String firstName, String lastName, String email, BigDecimal balance);
    
    Client updateBalance(Long id, BigDecimal newBalance);
}