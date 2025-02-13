package rating.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rating.model.Rating;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class RatingService {

    // TODO inject repository

    public List<Rating> getRatingsByMovieId(int movieId){
        log.info("about to retrieve ratings of a movie {}", movieId);
        List<Rating> ratings = null; //TODO query using repository
        return ratings;
    }

    public Rating addRating(Rating rating){
        log.info("about to persist rating {}", rating);

        if(!checkIfEMovieExists(rating.getMovieId())){
            throw new IllegalArgumentException("invalid movie id " + rating.getMovieId());
        }

        //TODO persist using repository
        log.info("rating persisted.");

        return rating;
    }

    private boolean checkIfEMovieExists(int movieId){
        return true;
    }
}
