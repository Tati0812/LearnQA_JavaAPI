import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class RestAssuredTest_Ex6 {

    @Description("Вывод адреса редиректа")
    @Test
    public void testRedirectAddress() {

        Response response = RestAssured
                .given()
                .redirects()
                .follow(true)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        String getHost = response.getHeader("x-host");
        System.out.println("\nискомый URL редиректа: " + getHost);
    }
}
