import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class RestAssuredTest_Ex5 {

    @Test
    public void testJsonSecondAnswer(){

        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .andReturn();

        String secondMessageText = response.jsonPath().getString("messages[1].message");
        System.out.println("\nТекст второго сообщения: " + secondMessageText);

    }

}
