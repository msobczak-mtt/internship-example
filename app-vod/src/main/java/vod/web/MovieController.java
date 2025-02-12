package vod.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vod.model.Cinema;
import vod.model.Movie;
import vod.service.CinemaService;
import vod.service.MovieService;
import vod.web.dto.MovieDto;
import vod.web.dto.MovieMapper;

import java.net.URI;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<MovieDto> addMovie(@RequestBody MovieDto movieDto) {
        log.info("About to add movie: {}", movieDto);

        // TODO validation

        // TODO service call
        Movie movie = movieMapper.fromDto(movieDto);
        movie = movieService.addMovie(movie);

        // TODO response preparation
        movieDto = movieMapper.toDto(movie);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .build(Map.of( "id", movie.getId()));

        return ResponseEntity.created(uri).body(movieDto);
    }
}
