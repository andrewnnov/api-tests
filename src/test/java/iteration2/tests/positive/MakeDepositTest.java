package iteration2.tests.positive;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import iteration2.steps.AccountStep;
import iteration2.steps.AuthStep;
import iteration2.steps.UserSteps;
import iteration2.utils.UserGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class MakeDepositTest {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    private String adminToken;
    private String userToken;
    private int accountId;

    @BeforeAll
    public static void setupRestAssured() {
        RestAssured.filters(List.of(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        ));
    }

    @BeforeEach
    public void setUp() {
        adminToken = AuthStep.login(ADMIN_USERNAME, ADMIN_PASSWORD);
        String generatedUserName = UserGenerator.generateUsername("User");
        userToken = UserSteps.createUser(adminToken, generatedUserName, "Kate5000!");
        accountId = AccountStep.createAccount(userToken);
    }

    @Test
    public void canDepositByAuthUser() {
        float balanceBeforeDeposit = AccountStep.getBalance(userToken,accountId);
        AccountStep.deposit(userToken, accountId, 100);
        float balanceAfterDeposit = AccountStep.getBalance(userToken,accountId);
        Assertions.assertEquals(balanceBeforeDeposit + 100, balanceAfterDeposit);
    }


}
