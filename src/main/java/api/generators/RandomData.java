package api.generators;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomData {
    private RandomData() {}

    public static String getUserName() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    public static String getPassword() {
        return RandomStringUtils.randomAlphabetic(3).toUpperCase() +
                RandomStringUtils.randomAlphabetic(3).toLowerCase() +
                RandomStringUtils.randomNumeric(3) + "!@";

    }

    public static Double getRandomAmount() {
        int value = ThreadLocalRandom.current().nextInt(1, 5001);
        return Double.valueOf(value);
    }
}
