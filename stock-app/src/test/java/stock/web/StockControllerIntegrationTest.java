package stock.web;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2")
class StockControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void should_get_all_stocks() {
        given()
                .when()
                .get("/api/stocks")
                .then()
                .statusCode(200)
                .body("$", hasSize(greaterThan(0)))
                .body("[0].symbol", notNullValue())
                .body("[0].companyName", notNullValue())
                .body("[0].currentPrice", notNullValue());
    }

    @Test
    void should_get_stock_by_id() {
        given()
                .when()
                .get("/api/stocks/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("symbol", equalTo("PKO"))
                .body("companyName", equalTo("PKO Bank Polski"));
    }

    @Test
    void should_return_404_for_non_existing_stock() {
        given()
                .when()
                .get("/api/stocks/999")
                .then()
                .statusCode(404);
    }

    @Test
    void should_get_stock_by_symbol() {
        given()
                .when()
                .get("/api/stocks/symbol/PKO")
                .then()
                .statusCode(200)
                .body("symbol", equalTo("PKO"))
                .body("companyName", equalTo("PKO Bank Polski"));
    }

    @Test
    void should_search_stocks_by_company_name() {
        given()
                .param("companyName", "Bank")
                .when()
                .get("/api/stocks/search")
                .then()
                .statusCode(200)
                .body("$", hasSize(greaterThan(0)))
                .body("[0].companyName", containsString("Bank"));
    }

    @Test
    void should_create_new_stock() {
        given()
                .param("symbol", "TEST")
                .param("companyName", "Test Company")
                .param("currentPrice", "100.00")
                .when()
                .post("/api/stocks")
                .then()
                .statusCode(200)
                .body("symbol", equalTo("TEST"))
                .body("companyName", equalTo("Test Company"))
                .body("currentPrice", equalTo(100.00f));
    }

    @Test
    void should_update_stock_price() {
        given()
                .param("newPrice", "45.00")
                .when()
                .put("/api/stocks/1/price")
                .then()
                .statusCode(200)
                .body("currentPrice", equalTo(45.00f))
                .body("lastUpdate", notNullValue());
    }
}