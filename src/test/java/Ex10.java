import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Ex10 {

    @ParameterizedTest
    @ValueSource(strings = {"This row more then 15 symbols","123456789012345"})
    public void testLength(String text){

        assertTrue(text.length() > 15, "Length is shorter or equal 15");
    }

}
