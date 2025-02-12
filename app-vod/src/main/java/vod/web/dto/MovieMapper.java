package vod.web.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vod.model.Movie;
import vod.service.MovieService;

@Component
@RequiredArgsConstructor
@Slf4j
public class MovieMapper {

    private final MovieService movieService;

    public MovieDto toDto(Movie movie) {
        return MovieDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .poster(movie.getPoster())
                .directorId(movie.getDirector().getId())
                .build();
    }

    public Movie fromDto(MovieDto movieDto) {
        Movie movie = new Movie();
        movie.setId(movieDto.getId());
        movie.setTitle(movieDto.getTitle());
        movie.setPoster(movieDto.getPoster());
        movie.setDirector(movieService.getDirectorById(movieDto.getDirectorId()));
        return movie;
    }
}
