package vod.repository.jpa;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import vod.model.Cinema;
import vod.model.Director;
import vod.model.Movie;
import vod.repository.MovieDao;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(value = "vod.dao", havingValue = "jpa")
@Slf4j
public abstract class JpaMovieDao implements MovieDao {

    private final EntityManager em;

    @Override
    public List<Movie> findAll() {
        return em.createQuery("from Movie", Movie.class).getResultList();
    }

    @Override
    public Optional<Movie> findById(Integer id) {
        return Optional.ofNullable(em.find(Movie.class, id));
    }

    @Override
    public List<Movie> findByDirector(Director d) {
        return em.createQuery("select m from Movie m where m.director=:director", Movie.class)
                .setParameter("director", d)
                .getResultList();
    }

    @Override
    public List<Movie> findByCinema(Cinema c) {
        return em.createQuery("select m from Movie m inner join m.cinemas cinema where cinema=:cinema", Movie.class)
                .setParameter("cinema", c)
                .getResultList();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Movie save(Movie m) {
        em.persist(m);
        return m;
    }
}
