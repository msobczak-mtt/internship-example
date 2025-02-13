package vod.repository.jdbc;

import lombok.RequiredArgsConstructor;
import vod.model.Cinema;
import vod.model.Director;
import vod.model.Movie;
import vod.repository.MovieDao;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
public class JdbcMovieDao implements MovieDao {

    private static final String SELECT_ALL_MOVIES =  "select m.id as movie_id, " +
            "m.title as movie_title, m.poster as movie_poster, m.director_id as movie_director_id from movie m";

    public static final String SELECT_MOVIE_BY_ID = "select m.id as movie_id, " +
            "m.title as movie_title, m.poster as movie_poster, m.director_id as movie_director_id from movie m where id=?";

    private static final String SELECT_MOVIES_BY_CINEMA =  "select m.id as movie_id, " +
            "m.title as movie_title, m.poster as movie_poster, m.director_id as movie_director_id " +
            "from movie m inner join movie_cinema mc on mc.movie_id=m.id " +
            "where mc.cinema_id=?";

    private final DataSource dataSource;

    @Override
    public List<Movie> findAll() {
        return null;
    }

    @Override
    public Optional<Movie> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public List<Movie> findByDirector(Director d) {
        return null;
    }

    @Override
    public List<Movie> findByCinema(Cinema c) {
        return null;
    }

    @Override
    public Movie save(Movie m) {
        return null;
    }

    public Movie mapMovie(ResultSet rs) throws SQLException {
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
