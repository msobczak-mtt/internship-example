package stock.repository.mem;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import stock.model.Stock;
import stock.repository.StockDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnProperty(value = "stock.dao", havingValue = "mem")
public class MemStockDao implements StockDao {

    @Override
    public List<Stock> findAll() {
        return SampleData.stocks;
    }

    @Override
    public Optional<Stock> findById(Long id) {
        return SampleData.stocks.stream()
                .filter(stock -> stock.getId().equals(id))
                .findFirst();
    }

    @Override
    public <S extends Stock> S save(S stock) {
        if (stock.getId() == null) {
            stock.setId(SampleData.getNextId(SampleData.stocks));
            SampleData.stocks.add(stock);
        } else {
            // Update existing
            SampleData.stocks.removeIf(s -> s.getId().equals(stock.getId()));
            SampleData.stocks.add(stock);
        }
        return stock;
    }

    @Override
    public void deleteById(Long id) {
        SampleData.stocks.removeIf(stock -> stock.getId().equals(id));
    }

    @Override
    public Optional<Stock> findBySymbol(String symbol) {
        return SampleData.stocks.stream()
                .filter(stock -> stock.getSymbol().equals(symbol))
                .findFirst();
    }

    @Override
    public List<Stock> findByCompanyNameContaining(String companyName) {
        return SampleData.stocks.stream()
                .filter(stock -> stock.getCompanyName().toLowerCase().contains(companyName.toLowerCase()))
                .toList();
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
        return SampleData.stocks.stream()
                .filter(stock -> {
                    for (Long id : ids) {
                        if (stock.getId().equals(id)) {
                            return true;
                        }
                    }
                    return false;
                })
                .toList();
    }

    @Override
    public long count() {
        return SampleData.stocks.size();
    }

    @Override
    public void delete(Stock entity) {
        SampleData.stocks.removeIf(stock -> stock.getId().equals(entity.getId()));
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
        SampleData.stocks.clear();
    }
}