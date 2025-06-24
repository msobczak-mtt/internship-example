package stock.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import stock.model.Index;
import stock.model.Stock;
import stock.repository.IndexDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnProperty(value = "stock.dao", havingValue = "jpa")
public class JpaIndexDao implements IndexDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Index> findAll() {
        return entityManager.createQuery("SELECT i FROM Index i", Index.class).getResultList();
    }

    @Override
    public Optional<Index> findById(Long id) {
        Index index = entityManager.find(Index.class, id);
        return Optional.ofNullable(index);
    }

    @Override
    public Optional<Index> findBySymbol(String symbol) {
        List<Index> indices = entityManager.createQuery(
                "SELECT i FROM Index i WHERE i.symbol = :symbol", Index.class)
                .setParameter("symbol", symbol)
                .getResultList();
        return indices.isEmpty() ? Optional.empty() : Optional.of(indices.get(0));
    }

    @Override
    public List<Index> findByStock(Stock stock) {
        return entityManager.createQuery(
                "SELECT i FROM Index i JOIN i.stocks s WHERE s = :stock", Index.class)
                .setParameter("stock", stock)
                .getResultList();
    }

    @Override
    public <S extends Index> S save(S entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
            return entity;
        } else {
            return entityManager.merge(entity);
        }
    }

    @Override
    public <S extends Index> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        entities.forEach(entity -> result.add(save(entity)));
        return result;
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    @Override
    public List<Index> findAllById(Iterable<Long> ids) {
        return entityManager.createQuery("SELECT i FROM Index i WHERE i.id IN :ids", Index.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(i) FROM Index i", Long.class)
                .getSingleResult();
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(entityManager::remove);
    }

    @Override
    public void delete(Index entity) {
        entityManager.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        ids.forEach(this::deleteById);
    }

    @Override
    public void deleteAll(Iterable<? extends Index> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM Index").executeUpdate();
    }
}