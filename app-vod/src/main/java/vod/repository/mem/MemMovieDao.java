package vod.repository.mem;

import org.springframework.stereotype.Component;
import vod.model.Cinema;
import vod.model.Director;
import vod.model.Movie;
import vod.repository.MovieDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MemMovieDao implements MovieDao {

    static List<Movie> movies = new ArrayList<>();

    @Override
    public List<Movie> findAll() {
        return movies;
    }

    @Override
    public Optional<Movie> findById(Integer id) {
        return movies.stream()
                .filter(m -> m.getId() == id)
                .findFirst();
    }

    @Override
    public List<Movie> findByDirector(Director d) {
        return movies.stream().filter(m -> m.getDirector() == d).collect(Collectors.toList());
    }

    @Override
    public List<Movie> findByCinema(Cinema c) {
        return movies.stream().filter(m -> m.getCinemas().contains(c)).collect(Collectors.toList());
    }

    @Override
    public Movie save(Movie m) {
        int max = movies.stream()
                .mapToInt(Movie::getId)
                .max()
                .orElse(0);
        m.setId(++max);
        movies.add(m);
        return m;
    }
}
