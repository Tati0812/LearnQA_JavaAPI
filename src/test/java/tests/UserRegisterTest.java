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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests= new ApiCoreRequests();

    @Epic("Создание пользователя")
    @Feature("Сценарии пользователя")
    @Description("Попытка создания пользователя с существующим email")
    @DisplayName("Негативный тест")
    @Test
    public void testCreateUserWithExistingEmail(){
        String email = "vinkotov@example.com";

        Map<String,String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData); // добавили метод наполенения данных о пользователе в рамках рефакторинга

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");

    }

    @Description("Успешное создание пользователя")
    @DisplayName("Позитивный тест")
    @Test
    public void testCreateUserSucessfully(){
        Map<String,String> userData = DataGenerator.getRegistrationData(); // добавили метод создания данных о пользователе в рамках рефакторинга

        Response responseCreateAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");

    }

    //Ex15
    @Description("Создание пользователя с некорректным email - без символа @")
    @DisplayName("Негативный тест")
    @Test
    public void testIncorrectEmailOnUserRegistration(){
        String email = "vinkotovexample123.com";

        Map<String,String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");

    }

    @Description("Создание пользователя без указания одного из полей. Отсутствие любого параметра не дает зарегистрировать пользователя")
    @DisplayName("Негативный тест")
    @ParameterizedTest
    @ValueSource(strings = {"username", "password", "firstName", "lastName", "email"})
    public void testCreateWithEmptyField(String field) {
        Map<String,String> userData = new HashMap<>();
        userData.put(field, null);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: " + field);

    }

    @Description("Создание пользователя с очень коротким именем в один символ")
    @DisplayName("Негативный тест")
    @Test
    public void testShortUsernameOnRegistration(){
        String username = "q";

        Map<String,String> userData = new HashMap<>();
        userData.put("username", username);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too short");

    }

    @Description("Создание пользователя с очень длинным именем - длиннее 250 символов")
    @DisplayName("Негативный тест")
    @Test
    public void testExtraLongUsernameOnRegistration(){
        String firstName = DataGenerator.getLongString(255);

        Map<String,String> userData = new HashMap<>();
        userData.put("firstName", firstName);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'firstName' field is too long");

    }

}
