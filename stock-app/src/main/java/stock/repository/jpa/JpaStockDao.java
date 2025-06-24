package stock.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import stock.model.Stock;
import stock.repository.StockDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnProperty(value = "stock.dao", havingValue = "jpa")
public class JpaStockDao implements StockDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Stock> findAll() {
        return entityManager.createQuery("SELECT s FROM Stock s", Stock.class).getResultList();
    }

    @Override
    public Optional<Stock> findById(Long id) {
        Stock stock = entityManager.find(Stock.class, id);
        return Optional.ofNullable(stock);
    }

    @Override
    public Optional<Stock> findBySymbol(String symbol) {
        List<Stock> stocks = entityManager.createQuery(
                "SELECT s FROM Stock s WHERE s.symbol = :symbol", Stock.class)
                .setParameter("symbol", symbol)
                .getResultList();
        return stocks.isEmpty() ? Optional.empty() : Optional.of(stocks.get(0));
    }

    @Override
    public List<Stock> findByCompanyNameContaining(String companyName) {
        return entityManager.createQuery(
                "SELECT s FROM Stock s WHERE s.companyName LIKE :companyName", Stock.class)
                .setParameter("companyName", "%" + companyName + "%")
                .getResultList();
    }

    @Override
    public <S extends Stock> S save(S entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
            return entity;
        } else {
            return entityManager.merge(entity);
        }
    }

    @Override
    public <S extends Stock> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        entities.forEach(entity -> result.add(save(entity)));
        return result;
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    @Override
    public List<Stock> findAllById(Iterable<Long> ids) {
        return entityManager.createQuery("SELECT s FROM Stock s WHERE s.id IN :ids", Stock.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(s) FROM Stock s", Long.class)
                .getSingleResult();
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(entityManager::remove);
    }

    @Override
    public void delete(Stock entity) {
        entityManager.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        ids.forEach(this::deleteById);
    }

    @Override
    public void deleteAll(Iterable<? extends Stock> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM Stock").executeUpdate();
    }
}