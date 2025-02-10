package vod.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import vod.config.VodConfig;
import vod.model.Cinema;
import vod.model.Movie;

import java.util.List;

@Slf4j
public class VodServiceMain {

    public static void main(String[] args) throws ClassNotFoundException {
        log.info("Let's find cinemas!");
        //Class.forName("vod.repository.mem.SampleData");

        // service preparation
       /* CinemaDao cinemaDao = new MemCinemaDao();
        MovieDao movieDao = new MemMovieDao();
        DirectorDao directorDao = new MemDirectorDao();
*/
        ApplicationContext context = new AnnotationConfigApplicationContext(VodConfig.class);
        CinemaService service = context.getBean(CinemaService.class);
                //new CinemaServiceBean(cinemaDao, movieDao);
        MovieService movieService = context.getBean(MovieService.class);
                //new MovieServiceBean(directorDao, cinemaDao, movieDao);

        // service use
        List<Cinema> cinemas = service.getAllCinemas();
        log.info(cinemas.size() + " cinemas found:");
        cinemas.forEach(cinema -> log.info("cinema: {}", cinema));

        Movie movie = movieService.getMovieById(2);
        log.info("Movie {}", movie.getTitle());


    }
}
