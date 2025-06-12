import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class RestAssuredTest_Ex6 {

    @Description("На самом деле, я не сильно поняла, что тут от меня требуется проверить, но попытаюсь")
    @Test
    public void testRedirectAddress() {

        String initialUrl = "https://playground.learnqa.ru/api/long_redirect";

        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get(initialUrl)
                .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println("\nStatus code: " + statusCode);
        if (statusCode != 200) {
            String finalRedirectedUrl = response.getHeader("Location");
            System.out.println("\nИскомый URL редиректа: " + finalRedirectedUrl);

            assertNotEquals(initialUrl, finalRedirectedUrl,
                    "Ожидалось, что URL изменится в результате редиректа, но он остался прежним.");
        }
    }
}
