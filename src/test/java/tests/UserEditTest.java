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

public class UserEditTest extends BaseTestCase {

    public final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Epic("Редактирование данных пользователя")
    @Feature("Сценарии пользователя")
    @Description("Позитивный тест")
    @DisplayName("Редактирование созданного пользователя")
    @Test
    public void testEditJustCreatedTest(){
        //GENERATE_USER
        Map<String,String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest(BASE_URL+"/user/", userData);

        String userId = getStringFromJson(responseCreateAuth, "id");

        //LOGIN
        Map<String,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest(BASE_URL+"/user/login", userData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        //EDIT
        String newName = "Changed name";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName",newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequest(BASE_URL+"/user/" + userId, header, cookie, editData );

        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequest(BASE_URL+"/user/" + userId, header, cookie);

        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }

    @DisplayName("Редактирование созданного пользователя без авторизации")
    @Description("Негативный тест")
    @Test
    public void testEditUserDataNotAuth(){
        //GENERATE USER
        Map<String,String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest(BASE_URL+"/user/", userData);

        String userId = getStringFromJson(responseCreateAuth, "id");

        //TRY EDITION
        String newName = "Changed name";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName",newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequest(BASE_URL+"/user/" + userId, null, null, editData );

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonByName(responseEditUser, "error", "Auth token not supplied");
    }

    @Description("Негативный тест")
    @DisplayName("Изменение данных пользователя, будучи авторизованным другим пользователем")
    @Test
    public void testTryEditByAnotherUser(){
        //GENERATE_FIRST_USER
        Map<String,String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest(BASE_URL+"/user/", userData);

        String userId = getStringFromJson(responseCreateAuth, "id");

        //LOGIN as first user
        Map<String,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest(BASE_URL+"/user/login", userData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");


        //GENERATE_SECOND_USER
        Map<String,String> userDataSecond = DataGenerator.getRegistrationData();

        Response responseCreateAuthForUserSecond = apiCoreRequests
                .makePostRequest(BASE_URL+"/user/", userDataSecond);

        String userIdSecond = getStringFromJson(responseCreateAuthForUserSecond, "id");

        //EDIT
        String newName = "Changed name";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName",newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequest(BASE_URL+"/user/" + userIdSecond, header, cookie, editData );

        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequest(BASE_URL+"/user/" + userId, header, cookie);

        Assertions.assertResponseCodeEquals(responseEditUser, 200);
        Assertions.assertJsonByName(responseEditUser, "error", "This user can only edit their own data.");
    }

    @Description("Негативный тест")
    @DisplayName("Изменить email пользователя, будучи авторизованными тем же пользователем, на новый email без символа @ ")
    @Test
    public void testTryEditEmailBySameUser(){
        //GENERATE_USER
        Map<String,String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest(BASE_URL+"/user/", userData);

        String userId = getStringFromJson(responseCreateAuth, "id");

        //LOGIN
        Map<String,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest(BASE_URL+"/user/login", userData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        //EDIT
        String newEmail = "learnqaexample.com";
        Map<String,String> editData = new HashMap<>();
        editData.put("email",newEmail);

        Response responseEditUser = apiCoreRequests
                .makePutRequest(BASE_URL+"/user/" + userId, header, cookie, editData );

        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequest(BASE_URL+"/user/" + userId, header, cookie);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonByName(responseEditUser, "error", "Invalid email format");
    }

    @Description("Негативный тест")
    @DisplayName("Изменение данных пользователя на короткое значение firstName")
    @Test
    public void testEditFirstNameToShortBySameUser(){
        //GENERATE_USER
        Map<String,String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest(BASE_URL+"/user/", userData);

        String userId = getStringFromJson(responseCreateAuth, "id");

        //LOGIN
        Map<String,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest(BASE_URL+"/user/login", userData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        //EDIT
        String newName = DataGenerator.getRandomNameByLenght(1);
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName",newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequest(BASE_URL+"/user/" + userId, header, cookie, editData );

        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequest(BASE_URL+"/user/" + userId, header, cookie);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonByName(responseEditUser, "error", "The value for field `firstName` is too short");
    }

}
