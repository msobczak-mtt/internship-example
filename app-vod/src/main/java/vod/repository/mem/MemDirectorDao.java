package vod.repository.mem;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import vod.model.Director;
import vod.repository.DirectorDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnProperty(value = "vod.dao", havingValue = "mem")
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
