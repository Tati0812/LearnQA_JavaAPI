package lib;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DataGenerator {
    public static String getRandomEmail(){
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss" + 1).format(new java.util.Date());
        return "learnqa" + timestamp + "@example.com";
    }

    public static Map<String,String> getRegistrationData(){
        Map<String,String> data = new HashMap<>();
        data.put("email", DataGenerator.getRandomEmail());
        data.put("password","123");
        data.put("username","learnqa");
        data.put("firstName","learnqa");
        data.put("lastName","learnqa");

        return data;

    }

    public static Map<String,String> getRegistrationData(Map<String,String> nonDefaultValues){
        Map<String,String> defaultValues = DataGenerator.getRegistrationData();

        Map<String,String> userData = new HashMap<>();
        String[] keys = {"email", "password", "username", "firstName", "lastName"};
        for (String key: keys) {
            if (nonDefaultValues.containsKey(key)) {
                userData.put(key, nonDefaultValues.get(key));
            } else {
                userData.put(key, defaultValues.get(key));
            }
        }
        return userData;
    }

    public static String getRandomNameByLenght(int count) {
        String CHAR_SET = "abcdefghijklmnopqrstuvwxyz";

        Random random = new Random();
        StringBuilder username = new StringBuilder();
        for (int i = 0; i < count; i++) {
            username.append(CHAR_SET.charAt(random.nextInt(CHAR_SET.length())));
        }
        return username.toString();
        }
}
