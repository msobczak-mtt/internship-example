package vod.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vod.model.Cinema;
import vod.service.CinemaService;
import vod.service.MovieService;
import vod.web.dto.MovieDto;
import vod.web.dto.MovieMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MovieController {

    private final MovieService movieService;
    private final CinemaService cinemaService;
    private final MovieMapper movieMapper;

    @GetMapping("/movies")
    public List<MovieDto> getMovies() {
        return movieService.getAllMovies().stream()
                .map(movieMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<MovieDto> getMovie(@PathVariable("id") int id) {
        return ResponseEntity.of(
                Optional.ofNullable(movieService.getMovieById(id))
                        .map(movieMapper::toDto));
    }

    @GetMapping("/cinemas/{id}/movies")
    public List<MovieDto> getMovies(@PathVariable("id") int id) {
        Cinema cinema = cinemaService.getCinemaById(id);
        return cinemaService.getMoviesInCinema(cinema).stream()
                .map(movieMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/movies")
    public MovieDto addMovie(@RequestBody MovieDto movieDto) {
        log.info("About to add movie: {}", movieDto);

        // TODO validation

        // TODO service call

        // TODO response preparation

        return movieDto;
    }
}
