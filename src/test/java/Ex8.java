import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import java.util.concurrent.TimeUnit;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Ex8 {

    @Test
    public void testLongRunningTask() {
        // 1) Создаем задачу
        Response response = given()
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job");

        response.then().statusCode(200); // Проверка статуса ответа
        JsonPath jsonPath = response.jsonPath();
        int secondsToWait = jsonPath.getInt("seconds");
        String token = jsonPath.getString("token");

        System.out.println("Задача создана. Токен: " + token + ", Время ожидания: " + secondsToWait + " секунд");

        // 2) Делаем запрос ДО того, как задача готова
        response = given()
                .queryParam("token", token)
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job");

        response.then().statusCode(200)
                .body("status", equalTo("Job is NOT ready")); // Проверяем статус

        System.out.println("Проверка статуса 'Job is NOT ready' пройдена.");

        // 3) Ждем нужное количество секунд (НЕ лучший подход, см. примечание выше)
        try {
            TimeUnit.SECONDS.sleep(secondsToWait);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Ожидание прервано: " + e.getMessage());
        }

        // 4) Делаем запрос ПОСЛЕ того, как задача готова
        response = given()
                .queryParam("token", token)
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job");

        response.then().statusCode(200)
                .body("status", equalTo("Job is ready"))
                .body("result", notNullValue()); // Проверяем статус и наличие результата

        System.out.println("Проверка статуса 'Job is ready' и наличия поля 'result' пройдена.");
        System.out.println("Результат: " + response.jsonPath().getString("result"));
    }
}