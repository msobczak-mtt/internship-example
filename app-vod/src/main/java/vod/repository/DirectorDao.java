package vod.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;
import vod.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorDao extends ListCrudRepository<Director, Integer> {

    //List<Director> findAll();

    //Optional<Director> findById(Integer id);

    //Director save(Director d);


}
