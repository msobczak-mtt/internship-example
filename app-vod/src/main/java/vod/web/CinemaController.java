package vod.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vod.model.Cinema;
import vod.service.CinemaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CinemaController {

    private final CinemaService cinemaService;

    @GetMapping("/cinemas")
    List<Cinema> getCinemas() {
        log.info("about to retrieve cinemas");
        List<Cinema> cinemas = cinemaService.getAllCinemas();

        return cinemas;
    }

}
