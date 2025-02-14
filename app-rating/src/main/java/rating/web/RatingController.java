package rating.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rating.model.Rating;
import rating.service.RatingService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping(value = "/ratings")
    List<Rating> getRatings(@RequestParam("movieId") Integer movieId) {
        log.info("about to retrieve ratings");

        return ratingService.getRatingsByMovieId(movieId);
    }

    @PostMapping("/ratings")
    ResponseEntity<?> createRating(@Validated @RequestBody Rating rating) {
        log.info("About to add rating: {}", rating);
        rating = ratingService.addRating(rating);

        return ResponseEntity.status(HttpStatus.CREATED).body(rating);
    }
}
