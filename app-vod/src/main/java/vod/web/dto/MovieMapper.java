package vod.web.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vod.model.Movie;

@Component
@RequiredArgsConstructor
@Slf4j
public class MovieMapper {

    public MovieDto toDto(Movie movie) {
        return MovieDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .poster(movie.getPoster())
                .directorId(movie.getDirector().getId())
                .build();
    }
}
