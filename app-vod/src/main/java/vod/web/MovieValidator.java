package vod.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import vod.service.MovieService;
import vod.web.dto.MovieDto;

@Component
@RequiredArgsConstructor
public class MovieValidator implements Validator {

    private final MovieService movieService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(MovieDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MovieDto movieDto = (MovieDto) target;

        if(movieService.getDirectorById(movieDto.getDirectorId())==null){
            errors.rejectValue("directorId", "errors.director.missing");
        }
    }
}
