package vod.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import vod.model.Cinema;
import vod.model.Director;
import vod.model.Movie;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MovieDao extends ListCrudRepository<Movie, Integer> {

    //List<Movie> findAll();

    //Optional<Movie> findById(Integer id);

    List<Movie> findAllByTitleContainingAndIdGreaterThan(String titlePart, int id);

    List<Movie> findByDirector(Director d);

    @Query("select m from Movie m join m.cinemas cinema where cinema=:cinema")
    List<Movie> findByCinema(@Param("cinema") Cinema c);

    //Movie save(Movie m);

}
