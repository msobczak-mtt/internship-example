package vod.repository;

import vod.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorDao {

    List<Director> findAll();

    Optional<Director> findById(Integer id);

    Director save(Director d);


}
