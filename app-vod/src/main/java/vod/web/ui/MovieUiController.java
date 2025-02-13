package vod.web.ui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vod.model.Cinema;
import vod.model.Director;
import vod.model.Movie;
import vod.service.CinemaService;
import vod.service.MovieService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ui")
public class MovieUiController {
    private final CinemaService cinemaService;
    private final MovieService movieService;

    @GetMapping("/movies")
    String getCinemas(Model model, @RequestParam(value = "cinemaId", required = false) Integer cinemaId, @RequestParam(value = "directorId", required = false) Integer directorId) {
        log.info("about to render movie list");
        List<Movie> movies;
        String title;
        if (cinemaId == null && directorId == null) {
            movies = movieService.getAllMovies();
            title = "All movies";
        } else if (cinemaId != null && directorId == null) {
            Cinema cinema = cinemaService.getCinemaById(cinemaId);
            movies = cinemaService.getMoviesInCinema(cinema);
            title = "Movies in cinema " + cinema.getName();
        } else {
            Director director = movieService.getDirectorById(directorId);
            movies = movieService.getMoviesByDirector(director);
            title = "Movies by director " + director.getFirstName() + " " + director.getLastName();
        }
        model.addAttribute("movies", movies);
        model.addAttribute("title", title);
        return "moviesView";
    }
}