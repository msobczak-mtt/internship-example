package stock.repository.mem;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import stock.model.Client;
import stock.model.Stock;
import stock.model.Transaction;
import stock.repository.TransactionDao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnProperty(value = "stock.dao", havingValue = "mem")
public class MemTransactionDao implements TransactionDao {

    @Override
    public List<Transaction> findAll() {
        return SampleData.transactions;
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return SampleData.transactions.stream()
                .filter(transaction -> transaction.getId().equals(id))
                .findFirst();
    }

    @Override
    public <S extends Transaction> S save(S transaction) {
        if (transaction.getId() == null) {
            transaction.setId(SampleData.getNextId(SampleData.transactions));
            SampleData.transactions.add(transaction);
        } else {
            // Update existing
            SampleData.transactions.removeIf(t -> t.getId().equals(transaction.getId()));
            SampleData.transactions.add(transaction);
        }
        return transaction;
    }

    @Override
    public void deleteById(Long id) {
        SampleData.transactions.removeIf(transaction -> transaction.getId().equals(id));
    }

    @Override
    public List<Transaction> findByClient(Client client) {
        return SampleData.transactions.stream()
                .filter(transaction -> transaction.getClient().getId().equals(client.getId()))
                .toList();
    }

    @Override
    public List<Transaction> findByStock(Stock stock) {
        return SampleData.transactions.stream()
                .filter(transaction -> transaction.getStock().getId().equals(stock.getId()))
                .toList();
    }

    @Override
    public List<Transaction> findByClientOrderByTransactionDateDesc(Client client) {
        return SampleData.transactions.stream()
                .filter(transaction -> transaction.getClient().getId().equals(client.getId()))
                .sorted(Comparator.comparing(Transaction::getTransactionDate).reversed())
                .toList();
    }

    @Override
    public <S extends Transaction> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        entities.forEach(entity -> result.add(save(entity)));
        return result;
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    @Override
    public List<Transaction> findAllById(Iterable<Long> ids) {
        return SampleData.transactions.stream()
                .filter(transaction -> {
                    for (Long id : ids) {
                        if (transaction.getId().equals(id)) {
                            return true;
                        }
                    }
                    return false;
                })
                .toList();
    }

    @Override
    public long count() {
        return SampleData.transactions.size();
    }

    @Override
    public void delete(Transaction entity) {
        SampleData.transactions.removeIf(transaction -> transaction.getId().equals(entity.getId()));
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        ids.forEach(this::deleteById);
    }

    @Override
    public void deleteAll(Iterable<? extends Transaction> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        SampleData.transactions.clear();
    }
}