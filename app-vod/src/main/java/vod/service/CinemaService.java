package vod.service;

import vod.model.Cinema;
import vod.model.Movie;

import java.util.List;

public interface CinemaService {

    // cinemas
    Cinema getCinemaById(int id);

    List<Cinema> getAllCinemas();

    // repertoire

    List<Cinema> getCinemasByMovie(Movie m);

    List<Movie> getMoviesInCinema(Cinema c);

    void includeMovieToCinema(Movie m, Cinema c);

    void excludeMovieFromCinema(Movie m, Cinema c);

}
