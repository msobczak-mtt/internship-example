package vod.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vod.model.Cinema;
import vod.model.Movie;
import vod.repository.CinemaDao;
import vod.repository.MovieDao;
import vod.service.CinemaService;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Slf4j
public class CinemaServiceBean implements CinemaService {

    private final CinemaDao cinemaDao;
    private final MovieDao movieDao;

    @Override
    public Cinema getCinemaById(int id) {
        log.info("searching cinema by id " + id);
        return cinemaDao.findById(id).orElse(null);
    }

    @Override
    public List<Movie> getMoviesInCinema(Cinema c) {
        log.info("searching movies played in cinema " + c.getId());
        return movieDao.findByCinema(c);
    }

    @Override
    public void includeMovieToCinema(Movie m, Cinema c) {
        m.addCinema(c);
        c.addMovie(m);
    }

    @Override
    public void excludeMovieFromCinema(Movie m, Cinema c) {
        m.getCinemas().remove(c);
        c.getMovies().remove(m);
    }

    @Override
    public List<Cinema> getAllCinemas() {
        log.info("searching all cinemas");
        return cinemaDao.findAll();
    }

    @Override
    public List<Cinema> getCinemasByMovie(Movie m) {
        log.info("searching cinemas by movie " + m.getId());
        return cinemaDao.findByMovie(m);
    }

}
