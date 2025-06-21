
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class Ex11 {

    @Test
    public void testHomeworkCookie() {
        String EXPECTED_COOKIE_NAME = "HomeWork";

        Response response = RestAssured
                .given()
                .when()
                .get("https://playground.learnqa.ru/api/homework_cookie");

        response.then().statusCode(200);

        Map<String,String> cookies = response.getCookies();
        System.out.println("cookies" + cookies);

        assertTrue(response.getCookies().size() > 0 , "Значение cookie не пустое");
        assertTrue(cookies.containsKey(EXPECTED_COOKIE_NAME) , "Значение cookie не пустое");

        System.out.println("Cookie '" + EXPECTED_COOKIE_NAME + "' найдена со значением: " + cookies.get(EXPECTED_COOKIE_NAME));

    }
}