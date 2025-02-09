package vod.repository.mem;

import vod.model.Cinema;
import vod.model.Movie;
import vod.repository.CinemaDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class MemCinemaDao implements CinemaDao {

    static List<Cinema> cinemas = new ArrayList<>();

    static {
        try {
            Class.forName("vod.repository.mem.SampleData");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Cinema> findAll() {
        return cinemas;
    }

    @Override
    public Optional<Cinema> findById(Integer id) {
        return cinemas.stream().filter(c -> c.getId() == id).findFirst();
    }

    @Override
    public List<Cinema> findByMovie(Movie m) {
        return cinemas.stream().filter(c -> c.getMovies().contains(m)).collect(Collectors.toList());
    }

    @Override
    public Cinema save(Cinema c) {
        int max = cinemas.stream()
                .mapToInt(Cinema::getId)
                .max()
                .orElse(0);

        c.setId(max + 1);
        cinemas.add(c);
        return c;
    }
}
