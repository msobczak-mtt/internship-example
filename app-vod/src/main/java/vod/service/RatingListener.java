package vod.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import vod.model.Movie;
import vod.repository.MovieDao;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class RatingListener {

    private final MovieDao movieDao;

    @KafkaListener(topics="ratings", groupId = "vod.group")
    void listenRateUpdate(RatingEvent ratingEvent ) {
        log.info("rating update received: {}", ratingEvent);

        Optional<Movie> movieMaybe  = movieDao.findById(ratingEvent.getMovieId());
        movieMaybe.ifPresent(movie -> {
            movie.setRate(ratingEvent.getRate());
            movieDao.save(movie);
        });
    }

    @Data
    static class RatingEvent{
        int movieId;
        double rate;
    }
}
