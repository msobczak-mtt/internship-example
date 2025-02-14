package vod.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import vod.model.Cinema;
import vod.repository.CinemaDao;
import vod.repository.MovieDao;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CinemaServiceBeanTest {

    @Mock
    private CinemaDao cinemaDao;

    @Mock
    private MovieDao movieDao;

    @Test
    void testCinemaPresent() {

        // given
        final int CINEMA_ID = 1;
        final String CINEMA_NAME = "Cinema";
        final String CINEMA_LOGO = "Cinema Logo";

        Mockito
                .when(cinemaDao.findById(CINEMA_ID))
                .thenReturn(Optional.of(prepareCinema(CINEMA_NAME, CINEMA_LOGO, CINEMA_ID)));

        CinemaServiceBean cinemaServiceBean = new CinemaServiceBean(cinemaDao, movieDao);

        // when
        Cinema cinema = cinemaServiceBean.getCinemaById(CINEMA_ID);

        // then
        assertNotNull(cinema);
        assertEquals(CINEMA_NAME, cinema.getName());
        assertEquals(CINEMA_LOGO, cinema.getLogo());
        assertEquals(CINEMA_ID, cinema.getId());
    }

    @Test
    void testCinemaMissing() {

        // given
        final int CINEMA_ID = 1;

        Mockito
                .when(cinemaDao.findById(CINEMA_ID))
                .thenReturn(Optional.empty());

        CinemaServiceBean cinemaServiceBean = new CinemaServiceBean(cinemaDao, movieDao);

        // when
        Cinema cinema = cinemaServiceBean.getCinemaById(CINEMA_ID);

        // then
        assertNull(cinema);
    }



    private Cinema prepareCinema(String name, String logo, int id){
        Cinema cinema = new Cinema();
        cinema.setName(name);
        cinema.setLogo(logo);
        cinema.setId(id);
        return cinema;
    }
}