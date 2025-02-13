package vod.repository.jdbc;

import vod.model.Director;
import vod.repository.DirectorDao;

import java.util.List;
import java.util.Optional;

public class JdbcDirectorDao implements DirectorDao {

    @Override
    public List<Director> findAll() {
        return List.of();
    }

    @Override
    public Optional<Director> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Director save(Director d) {
        return null;
    }
}
