package Lessons;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Lesson3_UserAuthNegativeTest {

    @Test
    public void testAuthUser(){
        Map<String,String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        Map<String,String> cookies = responseGetAuth.getCookies();
        Headers header = responseGetAuth.getHeaders();
        int userIdOnAuth = responseGetAuth.jsonPath().getInt("user_id");

        assertEquals(200, responseGetAuth.statusCode(), "Unexpected status code");
        assertTrue(cookies.containsKey("auth_sid"),"Response doesn't have 'auth_sid' cookie");
        assertTrue(header.hasHeaderWithName("x-crft-token"), "Response doesn't have 'x-crft-token' cookie" );
        assertTrue(responseGetAuth.jsonPath().getInt("userId") > 0 , "Response 'userId' should be greater then 0" );

        JsonPath responseCheckAuth = RestAssured
                .given()
                .header("x-crft-token", responseGetAuth.getHeader("x-crft-token"))
                .cookie("auth_sid")
                .get("https://playground.learnqa.ru/api/user/login")
                .jsonPath();

        int userIdOnCheck = responseCheckAuth.getInt("user_id");

        assertTrue(userIdOnCheck > 0 , "Unexpected user ID" + userIdOnCheck);
        assertEquals(userIdOnAuth, userIdOnCheck, "User Id from auth request is not equal to user ID from check request");

    }

    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})

    public void testNegativeAuthUser(String condition){
        Map<String,String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        Map<String,String> cookies = responseGetAuth.getCookies();
        Headers headers = responseGetAuth.getHeaders();

        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");

        if(condition.equals("cookie")) {
            spec.cookie("auth_sid", cookies.get("auth_sid"));
        } else if (condition.equals("headers")) {
            spec.header("x-crft-token", headers.get("x-crft-token"));
        } else {
            throw new IllegalArgumentException("Condition value is known:" + condition);
        }

        JsonPath responseForCheck = spec.get().jsonPath();
        assertEquals(0, responseForCheck.getInt("user_id"), "user id should be 0 for unauth request");
    }

}
