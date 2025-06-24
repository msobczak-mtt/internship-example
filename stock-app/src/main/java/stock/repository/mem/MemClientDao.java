package stock.repository.mem;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import stock.model.Client;
import stock.repository.ClientDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnProperty(value = "stock.dao", havingValue = "mem")
public class MemClientDao implements ClientDao {

    @Override
    public List<Client> findAll() {
        return SampleData.clients;
    }

    @Override
    public Optional<Client> findById(Long id) {
        return SampleData.clients.stream()
                .filter(client -> client.getId().equals(id))
                .findFirst();
    }

    @Override
    public <S extends Client> S save(S client) {
        if (client.getId() == null) {
            client.setId(SampleData.getNextId(SampleData.clients));
            SampleData.clients.add(client);
        } else {
            // Update existing
            SampleData.clients.removeIf(c -> c.getId().equals(client.getId()));
            SampleData.clients.add(client);
        }
        return client;
    }

    @Override
    public void deleteById(Long id) {
        SampleData.clients.removeIf(client -> client.getId().equals(id));
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        return SampleData.clients.stream()
                .filter(client -> client.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public <S extends Client> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        entities.forEach(entity -> result.add(save(entity)));
        return result;
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    @Override
    public List<Client> findAllById(Iterable<Long> ids) {
        return SampleData.clients.stream()
                .filter(client -> {
                    for (Long id : ids) {
                        if (client.getId().equals(id)) {
                            return true;
                        }
                    }
                    return false;
                })
                .toList();
    }

    @Override
    public long count() {
        return SampleData.clients.size();
    }

    @Override
    public void delete(Client entity) {
        SampleData.clients.removeIf(client -> client.getId().equals(entity.getId()));
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        ids.forEach(this::deleteById);
    }

    @Override
    public void deleteAll(Iterable<? extends Client> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        SampleData.clients.clear();
    }
}