package vod.repository;

import vod.model.Cinema;
import vod.model.Director;
import vod.model.Movie;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MovieDao {

    List<Movie> findAll();

    Optional<Movie> findById(Integer id);

    List<Movie> findByDirector(Director d);

    List<Movie> findByCinema(Cinema c);

    Movie save(Movie m);

}
