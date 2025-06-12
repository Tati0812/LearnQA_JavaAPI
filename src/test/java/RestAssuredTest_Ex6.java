import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class RestAssuredTest_Ex6 {

    @Description("Вывод адреса редиректа")
    @Test
    public void testRedirectAddress() {

        String initialUrl = "https://playground.learnqa.ru/api/long_redirect";

        Response response = RestAssured
                .given()
                .redirects()
                .follow(true)
                .when()
                .get(initialUrl)
                .andReturn();

        String finalRedirectedUrl = response.getHeader("x-host");
        System.out.println("\nискомый URL редиректа: " + finalRedirectedUrl);

        assertNotEquals(initialUrl, finalRedirectedUrl,
                "Ожидалось, что URL изменится в результате редиректа, но он остался прежним.");

    }
}
