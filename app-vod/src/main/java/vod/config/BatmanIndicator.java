package vod.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import vod.service.MovieService;

@Component
@RequiredArgsConstructor
public class BatmanIndicator implements HealthIndicator {

    private final MovieService movieService;
    @Override
    public Health health() {
        return movieService.getAllMovies().stream().anyMatch(m->m.getTitle().equals("Batman"))
                ? Health.up().build()
                : Health.down().build();
    }
}
