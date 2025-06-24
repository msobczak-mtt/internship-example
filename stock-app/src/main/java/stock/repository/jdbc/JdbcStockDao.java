package stock.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import stock.model.Stock;
import stock.repository.StockDao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnProperty(value = "stock.dao", havingValue = "jdbc")
public class JdbcStockDao implements StockDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static class StockRowMapper implements RowMapper<Stock> {
        @Override
        public Stock mapRow(ResultSet rs, int rowNum) throws SQLException {
            Stock stock = new Stock();
            stock.setId(rs.getLong("id"));
            stock.setSymbol(rs.getString("symbol"));
            stock.setCompanyName(rs.getString("company_name"));
            stock.setCurrentPrice(rs.getBigDecimal("current_price"));
            
            Timestamp lastUpdate = rs.getTimestamp("last_update");
            if (lastUpdate != null) {
                stock.setLastUpdate(lastUpdate.toLocalDateTime());
            }
            
            return stock;
        }
    }

    private final StockRowMapper stockRowMapper = new StockRowMapper();

    @Override
    public List<Stock> findAll() {
        return jdbcTemplate.query(
            "SELECT id, symbol, company_name, current_price, last_update FROM stock", 
            stockRowMapper
        );
    }

    @Override
    public Optional<Stock> findById(Long id) {
        List<Stock> stocks = jdbcTemplate.query(
            "SELECT id, symbol, company_name, current_price, last_update FROM stock WHERE id = ?",
            stockRowMapper, id
        );
        return stocks.isEmpty() ? Optional.empty() : Optional.of(stocks.get(0));
    }

    @Override
    public <S extends Stock> S save(S stock) {
        if (stock.getId() == null) {
            // INSERT
            String sql = "INSERT INTO stock (symbol, company_name, current_price, last_update) VALUES (?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, stock.getSymbol());
                ps.setString(2, stock.getCompanyName());
                ps.setBigDecimal(3, stock.getCurrentPrice());
                ps.setTimestamp(4, stock.getLastUpdate() != null ? 
                    Timestamp.valueOf(stock.getLastUpdate()) : Timestamp.valueOf(LocalDateTime.now()));
                return ps;
            }, keyHolder);

            stock.setId(keyHolder.getKey().longValue());
        } else {
            // UPDATE
            jdbcTemplate.update(
                "UPDATE stock SET symbol = ?, company_name = ?, current_price = ?, last_update = ? WHERE id = ?",
                stock.getSymbol(), stock.getCompanyName(), stock.getCurrentPrice(),
                Timestamp.valueOf(stock.getLastUpdate() != null ? stock.getLastUpdate() : LocalDateTime.now()),
                stock.getId()
            );
        }
        return stock;
    }

    @Override
    public Optional<Stock> findBySymbol(String symbol) {
        List<Stock> stocks = jdbcTemplate.query(
            "SELECT id, symbol, company_name, current_price, last_update FROM stock WHERE symbol = ?",
            stockRowMapper, symbol
        );
        return stocks.isEmpty() ? Optional.empty() : Optional.of(stocks.get(0));
    }

    @Override
    public List<Stock> findByCompanyNameContaining(String companyName) {
        return jdbcTemplate.query(
            "SELECT id, symbol, company_name, current_price, last_update FROM stock WHERE company_name LIKE ?",
            stockRowMapper, "%" + companyName + "%"
        );
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
        if (!ids.iterator().hasNext()) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder("SELECT id, symbol, company_name, current_price, last_update FROM stock WHERE id IN (");
        List<Object> params = new ArrayList<>();
        boolean first = true;
        for (Long id : ids) {
            if (!first) sql.append(",");
            sql.append("?");
            params.add(id);
            first = false;
        }
        sql.append(")");
        return jdbcTemplate.query(sql.toString(), stockRowMapper, params.toArray());
    }

    @Override
    public long count() {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM stock", Long.class);
        return count != null ? count : 0;
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM stock WHERE id = ?", id);
    }

    @Override
    public void delete(Stock entity) {
        if (entity.getId() != null) {
            deleteById(entity.getId());
        }
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
        jdbcTemplate.update("DELETE FROM stock");
    }
}