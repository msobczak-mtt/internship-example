package vod;

import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import vod.model.Cinema;
import vod.service.CinemaService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2")
@Slf4j
class VodApplicationTest {

    final int CINEMA_ID = 1;
    final String CINEMA_NAME = "Cinematheque Francaise";

    @Autowired
    private CinemaService cinemaService;

    @LocalServerPort
    private Integer localPort;

    @Test
    void testCinemaServiceReturningExistingCinema() {

        // given

        // when
        Cinema cinema = cinemaService.getCinemaById(CINEMA_ID);

        // then
        assertNotNull(cinema);
        assertEquals(CINEMA_NAME, cinema.getName());
        assertEquals(CINEMA_ID, cinema.getId());

    }

    @Test
    void testCinemaControllerReturningExistingCinema() {

        log.info("local server port: {}", localPort);

        RestAssured
                .get("http://localhost:" + localPort + "/cinemas/" + CINEMA_ID)
                .then()
                .statusCode(200).assertThat()
                .body("name", Matchers.equalTo(CINEMA_NAME))
                .body("id", Matchers.equalTo(CINEMA_ID));

    }


}