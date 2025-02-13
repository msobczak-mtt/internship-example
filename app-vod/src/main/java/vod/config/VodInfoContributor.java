package vod.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;
import vod.service.MovieService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class VodInfoContributor implements InfoContributor {

    private final MovieService movieService;
    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("Server time: ", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        builder.withDetail("Movies count:", movieService.getAllMovies().size());
    }
}
