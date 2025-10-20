package common.storage;

import api.models.CreateUserRequestModel;
import api.models.CreateUserResponseModel;
import api.requests.steps.UserSteps;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

public class SessionStorage {
    private static final SessionStorage INSTANCE = new SessionStorage();

    private final LinkedHashMap<CreateUserRequestModel, UserSteps> userStepsMap = new LinkedHashMap<>();
    private SessionStorage() {}

    public static void addUsers(List<CreateUserRequestModel> users) {
        for (CreateUserRequestModel user : users) {
            INSTANCE.userStepsMap.put(user, new UserSteps(user.getUsername(), user.getPassword()));
        }
    }

    /**
     * Возвращаем объект пользователя по его порядковому номеру в списке созданных пользователей
     * @param number Порядковый номер начиная с 1, а не с нуля
     * @return объект CreateUserRequestModel, соответствующий переданному индексу
     */
    public static CreateUserRequestModel getUser(int number) {
        return new ArrayList<>(INSTANCE.userStepsMap.keySet()).get(number - 1);
    }

    public static CreateUserRequestModel getUser() {
        return getUser(0);
    }

    public static UserSteps getSteps(int number) {
        return new ArrayList<>(INSTANCE.userStepsMap.values()).get(number - 1);
    }

    public static UserSteps getSteps() {
        return getSteps(1);
    }

    public static void clear() {
        INSTANCE.userStepsMap.clear();
    }


}
