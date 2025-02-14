package rating.service;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import rating.dao.RatingRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final RatingRepository ratingRepository;
    private final KafkaTemplate<String, RatingEvent> kafkaTemplate;

    public void notifyOnMovieratingUpdate(int movieId) {

        var averageRating = ratingRepository.findAllByMovieId(movieId).stream()
                .mapToDouble(rating->rating.getRate())
                .average()
                .orElseThrow();

        log.info("about to notify on movie {} rating update, average value is {}", movieId, averageRating);

        RatingEvent ratingEvent = RatingEvent.builder().movieId(movieId).rate(averageRating).build();
        kafkaTemplate.send("ratings", ratingEvent);
        log.info("Kafka notification successfully produced {}", ratingEvent);
    }

    @Data
    @Builder
    static class RatingEvent{
        int movieId;
        double rate;
    }
}
