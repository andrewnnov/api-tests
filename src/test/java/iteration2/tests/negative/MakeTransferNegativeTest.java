package iteration2.tests.negative;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import iteration2.steps.AccountStep;
import iteration2.steps.AuthStep;
import iteration2.steps.UserSteps;
import iteration2.utils.UserGenerator;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class MakeTransferNegativeTest {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String BASE_URL = "http://localhost:4111/api/v1";

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
    public void authUserCanNotTransferMoneyToOwnAccountMoreThatAccountHas() {
        float depositAmount = 200.0F;
        float transferAmount = 300.0F;
        AccountStep.deposit(userToken, senderAccountId, depositAmount);
        float balanceSenderBefore = AccountStep.getBalance(userToken, senderAccountId);
        float balanceReceiveBefore = AccountStep.getBalance(userToken, receiverAccountId);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("authorization", userToken)
                .body(String.format("""
                        {
                          "senderAccountId": %d,
                          "receiverAccountId": %d,
                          "amount": %.2f
                        }
                        """, senderAccountId, receiverAccountId, transferAmount))
                .post(BASE_URL + "/accounts/transfer")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.equalTo("Invalid transfer: insufficient funds or invalid accounts"));


        float balanceSenderAfter = AccountStep.getBalance(userToken, senderAccountId);
        float balanceReceiveAfter = AccountStep.getBalance(userToken, receiverAccountId);

        Assertions.assertEquals(balanceSenderBefore, balanceSenderAfter);
        Assertions.assertEquals(balanceReceiveBefore, balanceReceiveAfter);
    }

    @Test
    public void authUserCanNotTransferMoneyToOwnAccountNegativeAmount() {
        float depositAmount = 200.0F;
        float transferAmount = -300.0F;
        AccountStep.deposit(userToken, senderAccountId, depositAmount);
        float balanceSenderBefore = AccountStep.getBalance(userToken, senderAccountId);
        float balanceReceiveBefore = AccountStep.getBalance(userToken, receiverAccountId);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("authorization", userToken)
                .body(String.format("""
                        {
                          "senderAccountId": %d,
                          "receiverAccountId": %d,
                          "amount": %.2f
                        }
                        """, senderAccountId, receiverAccountId, transferAmount))
                .post(BASE_URL + "/accounts/transfer")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.equalTo("Invalid transfer: insufficient funds or invalid accounts"));


        float balanceSenderAfter = AccountStep.getBalance(userToken, senderAccountId);
        float balanceReceiveAfter = AccountStep.getBalance(userToken, receiverAccountId);

        Assertions.assertEquals(balanceSenderBefore, balanceSenderAfter);
        Assertions.assertEquals(balanceReceiveBefore, balanceReceiveAfter);
    }

    @Test
    public void authUserCanNotTransferMoneyToNotExistingAccount() {
        float depositAmount = 200.0F;
        float transferAmount = 100.0F;
        AccountStep.deposit(userToken, senderAccountId, depositAmount);
        float balanceSenderBefore = AccountStep.getBalance(userToken, senderAccountId);
        int notExistingAccountId = 5678;


        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("authorization", userToken)
                .body(String.format("""
                        {
                          "senderAccountId": %d,
                          "receiverAccountId": %d,
                          "amount": %.2f
                        }
                        """, senderAccountId, notExistingAccountId, transferAmount))
                .post(BASE_URL + "/accounts/transfer")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.equalTo("Invalid transfer: insufficient funds or invalid accounts"));


        float balanceSenderAfter = AccountStep.getBalance(userToken, senderAccountId);
        Assertions.assertEquals(balanceSenderBefore, balanceSenderAfter);

    }

    @Test
    public void transferFromOtherAccountShouldBeForbidden() {
        float depositAmount = 200.0F;
        float transferAmount = 300.0F;
        AccountStep.deposit(userToken, senderAccountId, depositAmount);
        float balanceSenderBefore = AccountStep.getBalance(userToken, senderAccountId);

        String generatedUserName = UserGenerator.generateUsername("User2");
        String userToken2 = UserSteps.createUser(adminToken, generatedUserName, "Kate5000!");
        int receiverAccountId2 = AccountStep.createAccount(userToken2);
        float balanceReceiverBefore = AccountStep.getBalance(userToken2, receiverAccountId2);


        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("authorization", userToken)
                .body(String.format("""
                        {
                          "senderAccountId": %d,
                          "receiverAccountId": %d,
                          "amount": %.2f
                        }
                        """, receiverAccountId2, senderAccountId, transferAmount))
                .post(BASE_URL + "/accounts/transfer")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body(Matchers.equalTo("Unauthorized access to account"));


        float balanceSenderAfter = AccountStep.getBalance(userToken, senderAccountId);
        float balanceReceiverAfter = AccountStep.getBalance(userToken2, receiverAccountId2);

        Assertions.assertEquals(balanceSenderBefore, balanceSenderAfter);
        Assertions.assertEquals(balanceReceiverBefore, balanceReceiverAfter);
    }



}
