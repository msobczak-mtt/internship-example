package vod.service;

import lombok.extern.slf4j.Slf4j;
import vod.model.Movie;
import vod.repository.CinemaDao;
import vod.repository.DirectorDao;
import vod.repository.MovieDao;
import vod.repository.mem.MemCinemaDao;
import vod.repository.mem.MemDirectorDao;
import vod.repository.mem.MemMovieDao;
import vod.model.Cinema;
import vod.service.impl.CinemaServiceBean;
import vod.service.impl.MovieServiceBean;

import java.util.List;

@Slf4j
public class VodServiceMain {

    public static void main(String[] args) throws ClassNotFoundException {
        log.info("Let's find cinemas!");
        Class.forName("vod.repository.mem.SampleData");

        // service preparation
        CinemaDao cinemaDao = new MemCinemaDao();
        MovieDao movieDao = new MemMovieDao();
        DirectorDao directorDao = new MemDirectorDao();

        CinemaService service = new CinemaServiceBean(cinemaDao, movieDao);
        MovieService movieService = new MovieServiceBean(directorDao, cinemaDao, movieDao);

        // service use
        List<Cinema> cinemas = service.getAllCinemas();
        log.info(cinemas.size() + " cinemas found:");
        cinemas.forEach(System.out::println);

        Movie movie = movieService.getMovieById(2);
        log.info("Movie {}", movie.getTitle());


    }
}
