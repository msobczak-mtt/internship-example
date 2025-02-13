package vod.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import vod.model.Cinema;
import vod.model.Movie;

import java.util.List;
import java.util.Optional;

public interface CinemaDao extends ListCrudRepository<Cinema, Integer> {

    //List<Cinema> findAll();

    //Optional<Cinema> findById(Integer id);

    @Query("select c from Cinema c join c.movies movie where movie=:movie")
    List<Cinema> findByMovie(@Param("movie") Movie m);

    //Cinema save(Cinema c);

}
