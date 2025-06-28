package lib;

import io.qameta.allure.Step;
import io.restassured.response.Response;


import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.hasKey;

public class Assertions {

    @Step("Make an assert of response json")
    public static void assertJsonByName(Response Response, String name, int expectedValue){
        Response.then().assertThat().body("$",hasKey(name));

        int value = Response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "JSON value isn't equal to expected value");
    }

    @Step("Make an assert of response json")
    public static void assertJsonByName(Response Response, String name, String expectedValue){
        Response.then().assertThat().body("$",hasKey(name));

        String value = Response.jsonPath().getString(name);
        assertEquals(expectedValue, value, "JSON value isn't equal to expected value");
    }

    @Step("Make an assert of response text")
    public static void assertResponseTextEquals(Response Response, String expectedAnswer){
        assertEquals(expectedAnswer, Response.asString(), "Response text is not as expected");
    }

    @Step("Make an assert of response text")
    public static void assertResponseTextEquals(Response Response, String keyValue, String expectedAnswer){
        assertEquals(expectedAnswer, Response.jsonPath().getString(keyValue), "Response text is not as expected");
    }

    @Step("Make an assert of response code")
    public static void assertResponseCodeEquals(Response Response, int expectedStatusCode){
        assertEquals(expectedStatusCode, Response.statusCode(), "Response statusCode is not as expected");
    }

    @Step("Make an assert of response json has required field")
    public static void assertJsonHasField(Response Response, String expectedFieldName){
        Response.then().assertThat().body("$",hasKey(expectedFieldName));
    }

    @Step("Make an assert of response json has required fields")
    public static void assertJsonHasFields(Response Response, String[] expectedFieldNames){
        for (String expectedFieldName : expectedFieldNames){
            Assertions.assertJsonHasField(Response, expectedFieldName);
        }
    }

    @Step("Make an assert of response json has no fields")
    public static void assertJsonHasNotFields(Response Response, String[] expectedFieldNames){
        for (String expectedFieldName : expectedFieldNames){
            Assertions.assertJsonHasNotField(Response, expectedFieldName);
        }
    }

    public static void assertJsonHasNotField(Response Response, String unexpectedFieldName){
        Response.then().assertThat().body("$",not(hasKey(unexpectedFieldName)));
    }

}
