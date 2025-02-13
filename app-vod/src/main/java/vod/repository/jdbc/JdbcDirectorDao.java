package vod.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import vod.model.Director;
import vod.repository.DirectorDao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
@RequiredArgsConstructor
public class JdbcDirectorDao implements DirectorDao {

    private static final String SELECT_ALL_DIRECTORS = "select d.id as director_id, d.firstname as director_first_name, d.lastname as director_last_name from director d";

    public static final String SELECT_DIRECTOR_BY_ID = "select d.id as director_id, d.firstname as director_first_name, d.lastname as director_last_name from director d where id=?";

    private final JdbcTemplate jdbcTemplate;


    @Override
    public List<Director> findAll() {
        return jdbcTemplate.query(SELECT_ALL_DIRECTORS, new DirectorMapper());
    }

    @Override
    public Optional<Director> findById(Integer id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_DIRECTOR_BY_ID, new DirectorMapper(), id));
    }

    @Override
    public Director save(Director d) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection->{
            PreparedStatement ps = connection.prepareStatement("INSERT INTO DIRECTOR(FIRSTNAME,LASTNAME) VALUES(?,?)", new String[]{"id"});
            ps.setString(1, d.getFirstName());
            ps.setString(2, d.getLastName());
            return ps;
        }, keyHolder);

        d.setId(keyHolder.getKey().intValue());

        return d;
    }

    static class DirectorMapper implements RowMapper<Director> {
        @Override
        public Director mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            Director d = new Director();
            d.setId(rs.getInt("director_id"));
            d.setFirstName(rs.getString("director_first_name"));
            d.setLastName(rs.getString("director_last_name"));
            return d;
        }
    }
}
