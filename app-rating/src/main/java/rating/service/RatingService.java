package rating.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rating.dao.RatingRepository;
import rating.model.Rating;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingService {

    private final RatingRepository ratingRepository;

    public List<Rating> getRatingsByMovieId(int movieId){
        log.info("about to retrieve ratings of a movie {}", movieId);
        List<Rating> ratings = ratingRepository.findAllByMovieId(movieId);
        return ratings;
    }

    public Rating addRating(Rating rating){
        log.info("about to persist rating {}", rating);

        if(!checkIfEMovieExists(rating.getMovieId())){
            throw new IllegalArgumentException("invalid movie id " + rating.getMovieId());
        }

        rating = ratingRepository.save(rating);
        log.info("rating persisted.");

        return rating;
    }

    private boolean checkIfEMovieExists(int movieId){
        return true;
    }
}
