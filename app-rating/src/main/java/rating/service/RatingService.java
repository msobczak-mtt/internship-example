package rating.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import rating.dao.RatingRepository;
import rating.model.Rating;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingService {

    private final RatingRepository ratingRepository;
    private final NotificationService notificationService;

    @Value("${vod.url}")
    private String vodUrl;

    public List<Rating> getRatingsByMovieId(int movieId) {
        log.info("about to retrieve ratings of a movie {}", movieId);
        List<Rating> ratings = ratingRepository.findAllByMovieId(movieId);
        return ratings;
    }

    public Rating addRating(Rating rating) {
        log.info("about to persist rating {}", rating);

        if (!checkIfEMovieExists(rating.getMovieId())) {
            throw new IllegalArgumentException("invalid movie id " + rating.getMovieId());
        }

        rating = ratingRepository.save(rating);
        log.info("rating persisted.");

        notificationService.notifyOnMovieratingUpdate(rating.getMovieId());
        return rating;
    }

    private boolean checkIfEMovieExists(int movieId) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }
        });

        String uri = vodUrl + "/movies/" + movieId;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        try {
            ResponseEntity<Map> result =
                    restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);
            return result.getStatusCode().is2xxSuccessful();//result.hasBody();
        }catch (Exception e) {
            log.error("error while movie verification", e);
            return false;
        }

    }
}
