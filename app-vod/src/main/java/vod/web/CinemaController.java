package vod.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vod.model.Cinema;
import vod.model.Movie;
import vod.service.CinemaService;
import vod.service.MovieService;
import vod.web.dto.CinemaDto;
import vod.web.dto.CinemaMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CinemaController {

    private final CinemaService cinemaService;
    private final MovieService movieService;
    private final CinemaMapper cinemaMapper;

    @GetMapping("/cinemas")
    List<CinemaDto> getCinemas(@RequestParam(value = "movieId", required = false) Integer movieId) {
        log.info("about to retrieve cinemas");
        List<Cinema> cinemas;
        if (movieId == null) {
            cinemas = cinemaService.getAllCinemas();
        } else {
            Movie movie = movieService.getMovieById(movieId);
            cinemas = cinemaService.getCinemasByMovie(movie);
        }

        return cinemas.stream()
                .map(cinemaMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/cinemas/{cinemaId}")
    ResponseEntity<CinemaDto> getCinema(@PathVariable("cinemaId") int cinemaId) {
        log.info("about to retrieve cinema {}", cinemaId);
        Cinema cinema = cinemaService.getCinemaById(cinemaId);

        return ResponseEntity.of(Optional.ofNullable(cinema).map(cinemaMapper::toDto));

       /* if (cinema != null) {
            return ResponseEntity
                    //.status(HttpStatus.OK)
                    .ok(cinemaMapper.toDto(cinema));
        } else {
            return ResponseEntity
                    .notFound()
                    //.status(HttpStatus.NOT_FOUND)
                    .build();
        }*/
    }

    @GetMapping("/movies/{movieId}/cinemas")
    List<CinemaDto> getCinemasShowingMovie(@PathVariable("movieId") int movieId) {
        log.info("about to retrieve cinemas showing movie {}", movieId);
        Movie movie = movieService.getMovieById(movieId);

        List<Cinema> cinemas = cinemaService.getCinemasByMovie(movie);

        return cinemas.stream()
                .map(cinemaMapper::toDto)
                .collect(Collectors.toList());
    }

}
