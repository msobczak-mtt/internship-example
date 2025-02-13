package vod.repository.jpa;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import vod.model.Director;
import vod.repository.DirectorDao;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(value = "vod.dao", havingValue = "jpa")
@Slf4j
public class JpaDirectorDao implements DirectorDao {

    private final EntityManager em;

    @Override
    public List<Director> findAll() {
        return em.createQuery("select d from Director d", Director.class).getResultList();
    }

    @Override
    public Optional<Director> findById(Integer id) {
        return Optional.ofNullable(em.find(Director.class, id));
    }

    @Override
    public Director save(Director d) {
        em.persist(d);
        return d;
    }
}
