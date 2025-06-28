package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserDeleteTest extends BaseTestCase {

    public final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Epic("Удаление пользователя")
    @Feature("Сценарии пользователя")
    @Description("Негативный тест")
    @DisplayName("Попытка удаления существующего пользователя когда авторизован")
    @Test
    public void testDeleteJustCreatedUser() {

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        //DELETE
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/2", header, cookie);

        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertJsonByName(
                responseDeleteUser,
                "error",
                "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");

    }

    @Description("Позитивный тест")
    @DisplayName("Удаление пользователя, когда он авторизован")
    @Test
    public void testDeleteAuthorizedUser(){
        //GENERATE_USER
        Map<String,String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        String userId = getStringFromJson(responseCreateAuth, "id");

        //LOGIN
        Map<String,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", userData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        //DELETE

        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/" + userId, header, cookie );

        Assertions.assertResponseCodeEquals(responseDeleteUser, 200);
        Assertions.assertJsonByName(responseDeleteUser, "success", "!");

        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId, header, cookie);

        Assertions.assertResponseTextEquals(responseUserData, "User not found");
    }

    @Description("Негативный тест")
    @DisplayName("Удаление пользователя, будучи авторизованным другим пользователем")
    @Test
    public void testTryDeleteByAnotherUser(){
        //GENERATE_FIRST_USER
        Map<String,String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        String userId = getStringFromJson(responseCreateAuth, "id");

        //LOGIN as first user
        Map<String,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", userData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        //GENERATE_SECOND_USER
        Map<String,String> userDataSecond = DataGenerator.getRegistrationData();

        Response responseCreateAuthForUserSecond = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userDataSecond);

        String userIdSecond = getStringFromJson(responseCreateAuthForUserSecond, "id");

        //DELETE

        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/" + userIdSecond, header, cookie );

        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertResponseTextEquals(responseDeleteUser, "error", "This user can only delete their own account.");

    }
}

