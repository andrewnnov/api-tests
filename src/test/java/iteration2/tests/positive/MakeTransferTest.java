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

public class MakeTransferTest {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    private String adminToken;
    private String userToken;
    private int senderAccountId;
    private int receiverAccountId;

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
        senderAccountId = AccountStep.createAccount(userToken);
        receiverAccountId = AccountStep.createAccount(userToken);
    }

    @Test
    public void authUserCanTransferMoneyToAnotherOwnAccount() {
        float deposit = 200.0F;
        float transfer = 100.0F;
        AccountStep.deposit(userToken, senderAccountId, deposit);
        float balanceSenderBefore = AccountStep.getBalance(userToken, senderAccountId);
        float balanceReceiveBefore = AccountStep.getBalance(userToken, receiverAccountId);

        AccountStep.transfer(userToken, senderAccountId, receiverAccountId, transfer);
        float balanceSenderAfter = AccountStep.getBalance(userToken, senderAccountId);
        float balanceReceiveAfter = AccountStep.getBalance(userToken, receiverAccountId);

        Assertions.assertEquals(balanceSenderBefore - transfer, balanceSenderAfter);
        Assertions.assertEquals(balanceReceiveBefore + transfer, balanceReceiveAfter);
    }

    @Test
    public void authUserCanTransferMoneyToAnotherUserAccount() {
        float deposit = 200.0F;
        float transfer = 100.0F;

        String generatedUserName = UserGenerator.generateUsername("User2");
        String userToken2 = UserSteps.createUser(adminToken, generatedUserName, "Kate5000!");
        int receiverAccountId2 = AccountStep.createAccount(userToken2);

        AccountStep.deposit(userToken, senderAccountId, deposit);
        float balanceSenderBefore = AccountStep.getBalance(userToken, senderAccountId);
        float balanceReceiveBefore = AccountStep.getBalance(userToken2, receiverAccountId2);

        AccountStep.transfer(userToken, senderAccountId, receiverAccountId2, transfer);
        float balanceSenderAfter = AccountStep.getBalance(userToken, senderAccountId);
        float balanceReceiveAfter = AccountStep.getBalance(userToken2, receiverAccountId2);

        Assertions.assertEquals(balanceSenderBefore - transfer, balanceSenderAfter);
        Assertions.assertEquals(balanceReceiveBefore + transfer, balanceReceiveAfter);
    }


}
