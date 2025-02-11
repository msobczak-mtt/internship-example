package vod;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import vod.model.Cinema;
import vod.model.Movie;
import vod.service.CinemaService;
import vod.service.MovieService;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class VodRunner implements CommandLineRunner {

    private final CinemaService cinemaService;
    private final MovieService movieService;

    @Override
    public void run(String... args) throws Exception {
        List<Cinema> cinemas = cinemaService.getAllCinemas();
        log.info(cinemas.size() + " cinemas found:");
        cinemas.forEach(cinema -> log.info("cinema: {}", cinema));

        Movie movie = movieService.getMovieById(2);
        log.info("Movie {}", movie.getTitle());
    }
}
