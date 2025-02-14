package vod;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import vod.model.Cinema;
import vod.service.CinemaService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
class VodApplicationTest {

    @Autowired
    private CinemaService cinemaService;

    @Test
    void contextLoads(){

        // given
        final int CINEMA_ID = 1;
        final String CINEMA_NAME = "Cinematheque Francaise";

        // when
        Cinema cinema = cinemaService.getCinemaById(CINEMA_ID);

        // then
        assertNotNull(cinema);
        assertEquals(CINEMA_NAME, cinema.getName());
        assertEquals(CINEMA_ID, cinema.getId());

    }

}