package stock.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import stock.model.Client;
import stock.model.Stock;
import stock.model.Transaction;
import stock.repository.TransactionDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnProperty(value = "stock.dao", havingValue = "jpa")
public class JpaTransactionDao implements TransactionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Transaction> findAll() {
        return entityManager.createQuery("SELECT t FROM Transaction t", Transaction.class).getResultList();
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        Transaction transaction = entityManager.find(Transaction.class, id);
        return Optional.ofNullable(transaction);
    }

    @Override
    public List<Transaction> findByClient(Client client) {
        return entityManager.createQuery(
                "SELECT t FROM Transaction t WHERE t.client = :client", Transaction.class)
                .setParameter("client", client)
                .getResultList();
    }

    @Override
    public List<Transaction> findByStock(Stock stock) {
        return entityManager.createQuery(
                "SELECT t FROM Transaction t WHERE t.stock = :stock", Transaction.class)
                .setParameter("stock", stock)
                .getResultList();
    }

    @Override
    public List<Transaction> findByClientOrderByTransactionDateDesc(Client client) {
        return entityManager.createQuery(
                "SELECT t FROM Transaction t WHERE t.client = :client ORDER BY t.transactionDate DESC", Transaction.class)
                .setParameter("client", client)
                .getResultList();
    }

    @Override
    public <S extends Transaction> S save(S entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
            return entity;
        } else {
            return entityManager.merge(entity);
        }
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
        return entityManager.createQuery("SELECT t FROM Transaction t WHERE t.id IN :ids", Transaction.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(t) FROM Transaction t", Long.class)
                .getSingleResult();
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(entityManager::remove);
    }

    @Override
    public void delete(Transaction entity) {
        entityManager.remove(entity);
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
        entityManager.createQuery("DELETE FROM Transaction").executeUpdate();
    }
}