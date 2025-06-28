package lib;

import io.qameta.allure.Step;
import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTestCase {
    @Step("Получаем headers из ответа по переданному ключу")
    protected String getHeader(Response Response, String name){
        Headers headers = Response.getHeaders();

        assertTrue(headers.hasHeaderWithName(name), "Response doesn't have header with name " + name);
        return headers.getValue(name);

    }

    @Step("Получаем cookies из ответа по переданному ключу")
    protected String getCookie(Response Response, String name){
        Map<String,String> cookies = Response.getCookies();

        assertTrue(cookies.containsKey(name), "Response doesn't have cookies with name " + name);
        return cookies.get(name);
    }

    @Step("Получаем число из ответа по переданному ключу")
    protected int getIntFromJson(Response Response, String name){
        Response.then().assertThat().body("$",hasKey(name));
        return Response.jsonPath().getInt(name);
    }

    @Step("Получаем строку из ответа по переданному ключу")
    protected String getStringFromJson(Response Response, String name) {
        Response.then().assertThat().body("$", hasKey(name));
        return Response.jsonPath().getString(name);
    }
}
