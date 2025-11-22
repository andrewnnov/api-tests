package common.utils;

import ui.elements.UserBage;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Принимаем на вход общего ретрая
 * 1. Что повторяем
 * 2. Условие выхода
 * 3. Максимальное количество попыток
 * 4. Интервал между попытками
 */
public class RetryUtils {
    public static <T> T retry(Supplier<T> action,
                              Predicate<T> condition,
                              int maxAttempts,
                              long intervalMillis) {

        T result = null;
        int attempts = 0;

        while (attempts < maxAttempts) {
            attempts++;
            result = action.get();
            if (condition.test(result)) {
                return result;
            }

            try {
                Thread.sleep(intervalMillis);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Max retry attempts reached without satisfying the condition.");
    }
}
