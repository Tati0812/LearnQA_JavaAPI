package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.Assertions;
import lib.BaseTestCase;
import lib.ApiCoreRequests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;

@Epic("Authorization cases")
@Feature("Authorization")
public class UserAuthTest extends BaseTestCase {

    String cookie;
    String header;
    int userIdOnAuth;

    private final ApiCoreRequests apiCoreRequests= new ApiCoreRequests();

    @BeforeEach
    public void loginUser(){
        Map<String,String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest(BASE_URL+"/user/login", authData);

        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth,"user_id");
    }

    @Description("This test successfully authorize user by email and password")
    @DisplayName("Test positive auth user")
    @Test
    public void testAuthUser(){

        Response responseCheckAuth = apiCoreRequests
                .makeGetRequest(BASE_URL+"/user/auth", this.header, this.cookie);

        Assertions.assertJsonByName(responseCheckAuth, "user_id", this.userIdOnAuth);
    }

    @Description("This test checks authorization status w/o sending cookie or token")
    @DisplayName("Test negative auth user")
    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    public void testNegativeAuthUser(String condition){

        RequestSpecification spec = RestAssured.given();
        spec.baseUri(BASE_URL+"/user/auth");

        if(condition.equals("cookie")) {
            Response responseForCheck = apiCoreRequests.makeGetRequestWithCookie(BASE_URL+"/user/auth", this.cookie);
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);
        } else if (condition.equals("headers")) {
            Response responseForCheck = apiCoreRequests.makeGetRequestWithToken(BASE_URL+"/user/auth", this.header);
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);
        } else {
            throw new IllegalArgumentException("Condition value is not known: " + condition);
        }
    }
}
