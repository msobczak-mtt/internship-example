package vod.repository.jpa;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import vod.model.Cinema;
import vod.model.Movie;
import vod.repository.CinemaDao;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(value = "vod.dao", havingValue = "jpa")
@Slf4j
public class JpaCinemaDao implements CinemaDao {

    private final EntityManager em;

    @Override
    public List<Cinema> findAll() {
        // SQL -> HQL -> JPQL
        return em.createQuery("select c from Cinema c").getResultList();
    }

    @Override
    public Optional<Cinema> findById(Integer id) {
        return Optional.ofNullable(em.find(Cinema.class, id));
    }

    @Override
    public List<Cinema> findByMovie(Movie m) {
        return em.createQuery("select c from Cinema c inner join c.movies movie where movie=:movie")
                .setParameter("movie", m)
                .getResultList();
    }

    @Override
    public Cinema save(Cinema c) {
        em.persist(c);
        return c;
    }
}
