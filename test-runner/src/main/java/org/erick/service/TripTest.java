package org.erick.service;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class TripTest {

    @Test
    public void mustCreateManyTrips() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(is("hello"));
    }
}
