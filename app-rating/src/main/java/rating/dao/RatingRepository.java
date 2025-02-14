package rating.dao;

import org.springframework.data.repository.ListCrudRepository;
import rating.model.Rating;

import java.util.List;

public interface RatingRepository extends ListCrudRepository<Rating, String> {

    List<Rating> findAllByMovieId(int movieId);
}
