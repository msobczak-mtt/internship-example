package vod.repository;

import vod.model.Cinema;
import vod.model.Movie;

import java.util.List;
import java.util.Optional;

public interface CinemaDao {

    List<Cinema> findAll();

    Optional<Cinema> findById(Integer id);

    List<Cinema> findByMovie(Movie m);

    Cinema save(Cinema c);

}
