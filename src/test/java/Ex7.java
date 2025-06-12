import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class Ex7 {

    @Description("На самом деле, я не сильно поняла, что тут от меня требуется проверить, но попытаюсь. Сага 2")
    @Test
    public void testRedirectAddress() {

        String initialUrl = "https://playground.learnqa.ru/api/long_redirect";
        String lastURL = null;
        int statusCode = 0;
        int redirectCount = 0;

        System.out.println("Начальный URL: " + initialUrl);

        while (statusCode != 200) {
        System.out.println("\nStatus code: " + statusCode);

            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(initialUrl)
                    .andReturn();

            statusCode = response.getStatusCode();
            lastURL = initialUrl;

            if (statusCode >= 300 && statusCode < 400) {

                String oneOfRedirectedLocation = response.getHeader("Location");

                initialUrl = oneOfRedirectedLocation;
                redirectCount++;
                System.out.println("Общее количество redirectCount: " + redirectCount);

                System.out.println("\nРедирект на: " + initialUrl);

                // Чтобы не зациклились
                if (redirectCount > 100) {
                    System.err.println("Превышено максимальное количество редиректов.");
                    break;
                }
            }
        }
        System.out.println("\nИскомый URL редиректа: " + lastURL);
    }
}
