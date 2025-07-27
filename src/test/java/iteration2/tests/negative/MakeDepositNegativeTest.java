package iteration2.tests.negative;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import iteration2.steps.AccountStep;
import iteration2.steps.AuthStep;
import iteration2.steps.UserSteps;
import iteration2.utils.UserGenerator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class MakeDepositNegativeTest {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String BASE_URL = "http://localhost:4111/api/v1";


    @BeforeAll
    public static void setupRestAssured() {
        RestAssured.filters(List.of(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        ));
    }

    @Test
    public void userCannotDepositToAnotherUserAccount() {
        String adminToken = AuthStep.login(ADMIN_USERNAME, ADMIN_PASSWORD);

        String generatedUserName1 = UserGenerator.generateUsername("User1");
        String userToken1 = UserSteps.createUser(adminToken, generatedUserName1, "Pass123!");
        int user1AccountId = AccountStep.createAccount(userToken1);

        String generatedUserName2 = UserGenerator.generateUsername("User2");
        String userToken2 = UserSteps.createUser(adminToken, generatedUserName2, "Pass123!");

        //user2 try to do deposit to user 1 account
        given()
                .header("authorization", userToken2)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(String.format("""
                        {
                          "id": %d,
                          "balance": 100.00
                        }
                        """, user1AccountId))
                .post(BASE_URL + "/accounts/deposit")
                .then()
                .statusCode(403)
                .body(Matchers.equalTo("Unauthorized access to account"));
    }

    @Test
    public void userCannotDepositToNotExistingUserAccountId() {
        String adminToken = AuthStep.login(ADMIN_USERNAME, ADMIN_PASSWORD);

        String generatedUserName1 = UserGenerator.generateUsername("User1");
        String userToken1 = UserSteps.createUser(adminToken, generatedUserName1, "Pass123!");
        int user1AccountId = AccountStep.createAccount(userToken1);
        int notExistingAccountId = 5678;

        given()
                .header("authorization", userToken1)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(String.format("""
                        {
                          "id": %d,
                          "balance": 200.00
                        }
                        """, notExistingAccountId))
                .post(BASE_URL + "/accounts/deposit")
                .then()
                .statusCode(403)
                .body(Matchers.equalTo("Unauthorized access to account"));
    }

    @Test
    public void userCannotMakeNegativeDeposit() {
        String adminToken = AuthStep.login(ADMIN_USERNAME, ADMIN_PASSWORD);

        String generatedUserName1 = UserGenerator.generateUsername("User1");
        String userToken1 = UserSteps.createUser(adminToken, generatedUserName1, "Pass123!");
        int user1AccountId = AccountStep.createAccount(userToken1);

        given()
                .header("authorization", userToken1)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(String.format("""
                        {
                          "id": %d,
                          "balance": -25.00
                        }
                        """, user1AccountId))
                .post(BASE_URL + "/accounts/deposit")
                .then()
                .statusCode(400)
                .body(Matchers.equalTo("Invalid account or amount"));
    }

    @Test
    public void userCannotMakeZeroDeposit() {
        String adminToken = AuthStep.login(ADMIN_USERNAME, ADMIN_PASSWORD);

        String generatedUserName1 = UserGenerator.generateUsername("User1");
        String userToken1 = UserSteps.createUser(adminToken, generatedUserName1, "Pass123!");
        int user1AccountId = AccountStep.createAccount(userToken1);

        given()
                .header("authorization", userToken1)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(String.format("""
                        {
                          "id": %d,
                          "balance": 0.00
                        }
                        """, user1AccountId))
                .post(BASE_URL + "/accounts/deposit")
                .then()
                .statusCode(400)
                .body(Matchers.equalTo("Invalid account or amount"));
    }

}
