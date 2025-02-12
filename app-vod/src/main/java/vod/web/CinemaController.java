package vod.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import vod.model.Cinema;
import vod.service.CinemaService;
import vod.web.dto.CinemaDto;
import vod.web.dto.CinemaMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CinemaController {

    private final CinemaService cinemaService;
    private final CinemaMapper cinemaMapper;

    @GetMapping("/cinemas")
    List<CinemaDto> getCinemas() {
        log.info("about to retrieve cinemas");
        List<Cinema> cinemas = cinemaService.getAllCinemas();
        return cinemas.stream()
                .map(cinemaMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/cinemas/{cinemaId}")
    CinemaDto getCinema(@PathVariable("cinemaId") int cinemaId){
        log.info("about to retrieve cinema {}", cinemaId);
        Cinema cinema = cinemaService.getCinemaById(cinemaId);
        return cinemaMapper.toDto(cinema);
    }

}
