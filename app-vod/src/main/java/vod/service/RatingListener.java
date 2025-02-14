package vod.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RatingListener {

    @KafkaListener(topics="ratings", groupId = "vod.group")
    void listenRateUpdate(RatingEvent ratingEvent ) {
        log.info("rating update received: {}", ratingEvent);
    }

    @Data
    static class RatingEvent{
        int movieId;
        double rate;
    }
}
