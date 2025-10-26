package api.generators;


import com.github.curiousoddman.rgxgen.RgxGen;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

public class RandomModelGenerator {
    private static final Random random = new Random();

    @SuppressWarnings("unchecked")
    public static <T> T generate(Class<T> clazz) {
        try {
            // Создание пустого объекта (через конструктор без аргументов)
            T instance = clazz.getDeclaredConstructor().newInstance();

            // Итерируем по полям
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);

                Object value = null;

                // Проверка на аннотацию GeneratingRule
                if (field.isAnnotationPresent(GeneratingRule.class)) {
                    GeneratingRule rule = field.getAnnotation(GeneratingRule.class);
                    String regex = rule.regex();
                    RgxGen rgxGen = new RgxGen(regex);
                    value = rgxGen.generate();
                } else {
                    // fallback генерация в зависимости от типа
                    Class<?> type = field.getType();
                    value = generateRandomValue(type);
                }

                field.set(instance, value);
            }

            return instance;

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при генерации модели: " + clazz.getSimpleName(), e);
        }
    }

    private static Object generateRandomValue(Class<?> type) {
        if (type.equals(String.class)) {
            return UUID.randomUUID().toString().substring(0, 8);
        } else if (type.equals(int.class) || type.equals(Integer.class)) {
            return random.nextInt(1000);
        } else if (type.equals(long.class) || type.equals(Long.class)) {
            return random.nextLong();
        } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            return random.nextBoolean();
        } else if (type.equals(double.class) || type.equals(Double.class)) {
            return random.nextDouble();
        } else if (type.equals(LocalDate.class)) {
            return LocalDate.now().minusDays(random.nextInt(365));
        }
        return null; // Для неподдерживаемых типов
    }
}
