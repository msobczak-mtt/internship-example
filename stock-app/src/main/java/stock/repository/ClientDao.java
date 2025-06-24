package stock.repository;

import org.springframework.data.repository.ListCrudRepository;
import stock.model.Client;

import java.util.Optional;

public interface ClientDao extends ListCrudRepository<Client, Long> {
    
    Optional<Client> findByEmail(String email);
}