package rating.service;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import rating.dao.RatingRepository;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableAsync
public class NotificationService {

    private final RatingRepository ratingRepository;
    private final KafkaTemplate<String, RatingEvent> kafkaTemplate;

    @Async
    public Future<Double> notifyOnMovieratingUpdate(int movieId) {

        var averageRating = ratingRepository.findAllByMovieId(movieId).stream()
                .mapToDouble(rating->rating.getRate())
                .average()
                .orElseThrow();

        log.info("about to notify on movie {} rating update, average value is {}", movieId, averageRating);

        try {
            Thread.sleep(5_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        RatingEvent ratingEvent = RatingEvent.builder().movieId(movieId).rate(averageRating).build();
        kafkaTemplate.send("ratings", ratingEvent);
        log.info("Kafka notification successfully produced {}", ratingEvent);
        return CompletableFuture.completedFuture(averageRating);
    }

    @Data
    @Builder
    static class RatingEvent{
        int movieId;
        double rate;
    }
}
