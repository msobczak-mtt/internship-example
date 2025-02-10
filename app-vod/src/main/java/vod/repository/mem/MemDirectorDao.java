package vod.repository.mem;

import org.springframework.stereotype.Component;
import vod.model.Director;
import vod.repository.DirectorDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MemDirectorDao implements DirectorDao {

    static List<Director> directors = new ArrayList<>();

    @Override
    public List<Director> findAll() {
        return directors;
    }

    @Override
    public Optional<Director> findById(Integer id) {
        return directors.stream()
                .filter(d -> d.getId() == id)
                .findFirst();
    }

    @Override
    public Director save(Director d) {
        int max = directors.stream()
                .mapToInt(Director::getId)
                .max()
                .orElse(0);
        d.setId(++max);
        directors.add(d);
        return d;
    }
}
