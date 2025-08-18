package iteration2.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class UserGenerator {
    public static String generateUsername(String prefix) {
        int random = new Random().nextInt(1000);
        return prefix + random;
    }
}
