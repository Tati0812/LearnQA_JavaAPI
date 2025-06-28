package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests= new ApiCoreRequests();

    @Epic("Получение данных пользователя")
    @DisplayName("Получение данных пользователя без авторизации")
    @Description("Негативный тест")
    @Test
    public void testGetUserDataNotAuth(){
        Response responseUserData = apiCoreRequests
                .makeGetRequest(BASE_URL+"/user/2");

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }

    @DisplayName("Получение данных авторизованного пользователя")
    @Description("Позитивный тест")
    @Test
    public void testGetUserDetailsAuthAsSameUser(){
        Map<String,String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest(BASE_URL+"/user/login", authData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookies = this.getCookie(responseGetAuth,"auth_sid");

        Response responseUserData = apiCoreRequests.makeGetRequest(BASE_URL+"/user/2", header, cookies);

        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        
        Assertions.assertJsonHasFields(responseUserData, expectedFields);
    }

    @DisplayName("Получение данных другого пользователя при запросе от авторизованного пользователя")
    @Description("Негативный тест")
    @Test
    public void testGetAnotherUserDetailsWhenAuth(){
        Map<String,String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest(BASE_URL+"/user/login", authData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookies = this.getCookie(responseGetAuth,"auth_sid");

        Response responseUserData = apiCoreRequests.makeGetRequest(BASE_URL+"/user/3", header, cookies);

        String[] expectedFields = {"firstName", "lastName", "email"};

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotFields(responseUserData, expectedFields);
    }
}
