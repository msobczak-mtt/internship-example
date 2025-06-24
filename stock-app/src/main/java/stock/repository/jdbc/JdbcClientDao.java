package stock.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import stock.model.Client;
import stock.repository.ClientDao;

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
public class JdbcClientDao implements ClientDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static class ClientRowMapper implements RowMapper<Client> {
        @Override
        public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
            Client client = new Client();
            client.setId(rs.getLong("id"));
            client.setFirstName(rs.getString("first_name"));
            client.setLastName(rs.getString("last_name"));
            client.setEmail(rs.getString("email"));
            client.setBalance(rs.getBigDecimal("balance"));
            
            Timestamp registrationDate = rs.getTimestamp("registration_date");
            if (registrationDate != null) {
                client.setRegistrationDate(registrationDate.toLocalDateTime());
            }
            
            return client;
        }
    }

    private final ClientRowMapper clientRowMapper = new ClientRowMapper();

    @Override
    public List<Client> findAll() {
        return jdbcTemplate.query(
            "SELECT id, first_name, last_name, email, balance, registration_date FROM client", 
            clientRowMapper
        );
    }

    @Override
    public Optional<Client> findById(Long id) {
        List<Client> clients = jdbcTemplate.query(
            "SELECT id, first_name, last_name, email, balance, registration_date FROM client WHERE id = ?",
            clientRowMapper, id
        );
        return clients.isEmpty() ? Optional.empty() : Optional.of(clients.get(0));
    }

    @Override
    public <S extends Client> S save(S client) {
        if (client.getId() == null) {
            // INSERT
            String sql = "INSERT INTO client (first_name, last_name, email, balance, registration_date) VALUES (?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, client.getFirstName());
                ps.setString(2, client.getLastName());
                ps.setString(3, client.getEmail());
                ps.setBigDecimal(4, client.getBalance());
                ps.setTimestamp(5, client.getRegistrationDate() != null ? 
                    Timestamp.valueOf(client.getRegistrationDate()) : Timestamp.valueOf(LocalDateTime.now()));
                return ps;
            }, keyHolder);

            client.setId(keyHolder.getKey().longValue());
        } else {
            // UPDATE
            jdbcTemplate.update(
                "UPDATE client SET first_name = ?, last_name = ?, email = ?, balance = ?, registration_date = ? WHERE id = ?",
                client.getFirstName(), client.getLastName(), client.getEmail(), client.getBalance(),
                Timestamp.valueOf(client.getRegistrationDate() != null ? client.getRegistrationDate() : LocalDateTime.now()),
                client.getId()
            );
        }
        return client;
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        List<Client> clients = jdbcTemplate.query(
            "SELECT id, first_name, last_name, email, balance, registration_date FROM client WHERE email = ?",
            clientRowMapper, email
        );
        return clients.isEmpty() ? Optional.empty() : Optional.of(clients.get(0));
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
        if (!ids.iterator().hasNext()) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder("SELECT id, first_name, last_name, email, balance, registration_date FROM client WHERE id IN (");
        List<Object> params = new ArrayList<>();
        boolean first = true;
        for (Long id : ids) {
            if (!first) sql.append(",");
            sql.append("?");
            params.add(id);
            first = false;
        }
        sql.append(")");
        return jdbcTemplate.query(sql.toString(), clientRowMapper, params.toArray());
    }

    @Override
    public long count() {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM client", Long.class);
        return count != null ? count : 0;
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM client WHERE id = ?", id);
    }

    @Override
    public void delete(Client entity) {
        if (entity.getId() != null) {
            deleteById(entity.getId());
        }
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
        jdbcTemplate.update("DELETE FROM client");
    }
}