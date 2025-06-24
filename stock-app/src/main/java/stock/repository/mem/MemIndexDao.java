package stock.repository.mem;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import stock.model.Index;
import stock.model.Stock;
import stock.repository.IndexDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnProperty(value = "stock.dao", havingValue = "mem")
public class MemIndexDao implements IndexDao {

    @Override
    public List<Index> findAll() {
        return SampleData.indices;
    }

    @Override
    public Optional<Index> findById(Long id) {
        return SampleData.indices.stream()
                .filter(index -> index.getId().equals(id))
                .findFirst();
    }

    @Override
    public <S extends Index> S save(S index) {
        if (index.getId() == null) {
            index.setId(SampleData.getNextId(SampleData.indices));
            SampleData.indices.add(index);
        } else {
            // Update existing
            SampleData.indices.removeIf(i -> i.getId().equals(index.getId()));
            SampleData.indices.add(index);
        }
        return index;
    }

    @Override
    public void deleteById(Long id) {
        SampleData.indices.removeIf(index -> index.getId().equals(id));
    }

    @Override
    public Optional<Index> findBySymbol(String symbol) {
        return SampleData.indices.stream()
                .filter(index -> index.getSymbol().equals(symbol))
                .findFirst();
    }

    @Override
    public List<Index> findByStock(Stock stock) {
        return SampleData.indices.stream()
                .filter(index -> index.getStocks().stream()
                        .anyMatch(s -> s.getId().equals(stock.getId())))
                .toList();
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
        return SampleData.indices.stream()
                .filter(index -> {
                    for (Long id : ids) {
                        if (index.getId().equals(id)) {
                            return true;
                        }
                    }
                    return false;
                })
                .toList();
    }

    @Override
    public long count() {
        return SampleData.indices.size();
    }

    @Override
    public void delete(Index entity) {
        SampleData.indices.removeIf(index -> index.getId().equals(entity.getId()));
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
        SampleData.indices.clear();
    }
}