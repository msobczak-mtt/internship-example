package vod.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import vod.model.Cinema;
import vod.model.Movie;
import vod.repository.CinemaDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
@RequiredArgsConstructor
public class JdbcCinemaDao implements CinemaDao {

    private static final String SELECT_ALL_CINEMAS = "select c.id as cinema_id, " +
            "c.name as cinema_name, c.logo as cinema_logo from cinema c";

    public static final String SELECT_CINEMA_BY_ID = "select c.id as cinema_id, " +
            "c.name as cinema_name, c.logo as cinema_logo from cinema c where id=?";

    private static final String SELECT_CINEMAS_BY_MOVIE = "select c.id as cinema_id, " +
            "c.name as cinema_name, c.logo as cinema_logo " +
            "from cinema c inner join movie_cinema mc on mc.cinema_id=c.id " +
            "where mc.movie_id=?";


    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Cinema> findAll() {
        return jdbcTemplate.query(SELECT_ALL_CINEMAS, new CinemaMapper());

        /*new ArrayList<>();

        try(Connection connection = dataSource.getConnection(); Statement stmt = connection.createStatement();){
            ResultSet rs = stmt.executeQuery(SELECT_ALL_CINEMAS);
            while(rs.next()){
                cinemas.add(mapRow(rs, 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public Optional<Cinema> findById(Integer id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_CINEMA_BY_ID, new CinemaMapper(), id));
    }

    @Override
    public List<Cinema> findByMovie(Movie m) {
        return jdbcTemplate.query(SELECT_CINEMAS_BY_MOVIE, new CinemaMapper(), m.getId());
    }

    @Override
    public Cinema save(Cinema c) {
        return null;
    }


    static class CinemaMapper implements RowMapper<Cinema> {

        public Cinema mapRow(ResultSet rs, int rowNum) throws SQLException {
            Cinema c = new Cinema();
            c.setId(rs.getInt("cinema_id"));
            c.setName(rs.getString("cinema_name"));
            c.setLogo(rs.getString("cinema_logo"));
            return c;
        }

    }

}
