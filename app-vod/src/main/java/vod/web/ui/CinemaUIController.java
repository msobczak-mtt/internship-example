package vod.web.ui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vod.model.Cinema;
import vod.model.Movie;
import vod.service.CinemaService;
import vod.service.MovieService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ui")
public class CinemaUIController {

    private final CinemaService cinemaService;
    private final MovieService movieService;

    @GetMapping("/cinemas")
    String getCinemas(Model model, @RequestParam(value = "movieId", required = false) Integer movieId){
        log.info("about to render cinema list");

        List<Cinema> cinemas;
        String title;

        if(movieId == null){
            cinemas = cinemaService.getAllCinemas();
            title = "All cinemas";
        } else {
            Movie movie = movieService.getMovieById(movieId);
            cinemas = cinemaService.getCinemasByMovie(movie);
            title = "Cinemas showing " + movie.getTitle();
        }

        model.addAttribute("cinemas", cinemas);
        model.addAttribute("title", title);

        return "cinemasView";
    }
}
