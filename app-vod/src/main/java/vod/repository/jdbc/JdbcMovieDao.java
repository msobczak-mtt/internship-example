package vod.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import vod.model.Cinema;
import vod.model.Director;
import vod.model.Movie;
import vod.repository.MovieDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


@Repository
@ConditionalOnProperty(value = "vod.dao", havingValue = "jdbc")
@RequiredArgsConstructor
public class JdbcMovieDao implements MovieDao {

    private static final String SELECT_ALL_MOVIES = "select m.id as movie_id, " +
            "m.title as movie_title, m.poster as movie_poster, m.director_id as movie_director_id from movie m";

    public static final String SELECT_MOVIE_BY_ID = "select m.id as movie_id, " +
            "m.title as movie_title, m.poster as movie_poster, m.director_id as movie_director_id from movie m where id=?";

    private static final String SELECT_MOVIES_BY_CINEMA = "select m.id as movie_id, " +
            "m.title as movie_title, m.poster as movie_poster, m.director_id as movie_director_id " +
            "from movie m inner join movie_cinema mc on mc.movie_id=m.id " +
            "where mc.cinema_id=?";

    private static final String SELECT_MOVIES_BY_DIRECTOR = "select m.id as movie_id, " +
            "m.title as movie_title, m.poster as movie_poster, m.director_id as movie_director_id " +
            "from movie m where m.director_id=?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Movie> findAll() {
        return jdbcTemplate.query(SELECT_ALL_MOVIES, new MovieMapper());
    }

    @Override
    public Optional<Movie> findById(Integer id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_MOVIE_BY_ID, new MovieMapper(), id));
    }

    @Override
    public List<Movie> findByDirector(Director d) {
        return jdbcTemplate.query(SELECT_MOVIES_BY_DIRECTOR, new MovieMapper(), d.getId());
    }

    @Override
    public List<Movie> findByCinema(Cinema c) {
        return jdbcTemplate.query(SELECT_MOVIES_BY_CINEMA, new MovieMapper(), c.getId());
    }

    @Override
    public Movie save(Movie m) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO MOVIE(TITLE,POSTER,DIRECTOR_ID) VALUES(?,?,?)", new String[]{"id"});
            ps.setString(1, m.getTitle());
            ps.setString(2, m.getPoster());
            ps.setInt(3, m.getDirector().getId());
            return ps;
        }, keyHolder);
        //.update("INSERT INTO MOVIE(TITLE,POSTER,DIRECTOR_ID) VALUES(?,?,?)", m.getTitle(), m.getPoster(), m.getDirector().getId());

        m.setId(keyHolder.getKey().intValue());

        return m;
    }


    static class MovieMapper implements RowMapper<Movie> {

        @Override
        public Movie mapRow(ResultSet rs, int rowNum) throws SQLException {
            Movie m = new Movie();
            m.setId(rs.getInt("movie_id"));
            m.setTitle(rs.getString("movie_title"));
            m.setPoster(rs.getString("movie_poster"));
            Director director = new Director();
            director.setId(rs.getInt("movie_director_id"));
            m.setDirector(director);
            return m;
        }
    }
}
