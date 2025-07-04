import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Ex12 {

    @Test
    public void testHeaders(){
        Response response = RestAssured
                .given()
                .when()
                .get("https://playground.learnqa.ru/api/homework_header");

        Headers headers = response.getHeaders();

        assertTrue(headers.hasHeaderWithName("x-secret-homework-header"), "Response doesn't have 'x-secret-homework-header'" );
        assertEquals("Some secret value", headers.getValue("x-secret-homework-header"), "Header is wrong!");

    }
}
