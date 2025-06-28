package Lessons;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HelloWorldTest3 {

    @Test
    public void testRestAssuredSuccess(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/map")
                .andReturn();

        assertTrue(response.statusCode() == 200, "Unexpected status code");
    }

    @Test
    public void testRestAssuredAsserEqualsFailed(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/map2")
                .andReturn();

        assertEquals(200, response.statusCode(), "Unexpected status code");
    }

    @Description("Параметризованный тест")
    @Test
    public void testHelloMethodWithoutName(){
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();

        String answer = response.getString("answer");
        assertEquals("Hello, someone", answer, "The answer is not expected");
    }

    @Description("Параметризованный тест")
    @Test
    public void testHelloMethodWithName(){

        String name = "Username";

        JsonPath response = RestAssured
                .given()
                .queryParam("name", name)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();

        String answer = response.getString("answer");
        assertEquals("Hello, " + name, answer, "The answer is not expected");
    }
}
