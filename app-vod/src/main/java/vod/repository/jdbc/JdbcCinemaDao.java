package vod.repository.jdbc;

import lombok.RequiredArgsConstructor;
import vod.model.Cinema;
import vod.model.Movie;
import vod.repository.CinemaDao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
public class JdbcCinemaDao implements CinemaDao {

    private static final String SELECT_ALL_CINEMAS =  "select c.id as cinema_id, " +
            "c.name as cinema_name, c.logo as cinema_logo from cinema c";

    public static final String SELECT_CINEMA_BY_ID = "select c.id as cinema_id, " +
            "c.name as cinema_name, c.logo as cinema_logo from cinema c where id=?";

    private static final String SELECT_CINEMAS_BY_MOVIE =  "select c.id as cinema_id, " +
            "c.name as cinema_name, c.logo as cinema_logo " +
            "from cinema c inner join movie_cinema mc on mc.cinema_id=c.id " +
            "where mc.movie_id=?";


    private final DataSource dataSource;

    @Override
    public List<Cinema> findAll() {
        List<Cinema> cinemas = new ArrayList<>();

        try(Connection connection = dataSource.getConnection(); Statement stmt = connection.createStatement();){
            ResultSet rs = stmt.executeQuery(SELECT_ALL_CINEMAS);
            while(rs.next()){
                cinemas.add(mapRow(rs, 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cinemas;
    }

    @Override
    public Optional<Cinema> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public List<Cinema> findByMovie(Movie m) {
        return List.of();
    }

    @Override
    public Cinema save(Cinema c) {
        return null;
    }

    public Cinema mapRow(ResultSet rs, int rowNum) throws SQLException {
        Cinema c = new Cinema();
        c.setId(rs.getInt("cinema_id"));
        c.setName(rs.getString("cinema_name"));
        c.setLogo(rs.getString("cinema_logo"));
        return c;
    }

}
