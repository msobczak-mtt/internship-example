package vod.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class WeekendIndicator implements HealthIndicator {

    @Override
    public Health health() {
        return LocalDate.now().getDayOfWeek().ordinal() < 5
                ? Health.up().build()
                : Health.down().build();
    }
}
