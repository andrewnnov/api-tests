package iteration2.steps;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;

import static io.restassured.RestAssured.given;

public class AccountStep {
    private static final String BASE_URL = "http://localhost:4111/api/v1";

    public static int createAccount(String token) {
        return given()
                .header("authorization", token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .post(BASE_URL + "/accounts")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().path("id");
    }

    public static void deposit(String token, int accountId, float amount) {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("authorization", token)
                .body(String.format("""
                        {
                          "id": %d,
                          "balance": %.2f
                        }
                        """, accountId, amount))
                .post(BASE_URL + "/accounts/deposit")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("balance", Matchers.equalTo((float) amount));
    }

    public static void transfer(String token, int senderAccountId, int receiverAccountId,  float amount) {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("authorization", token)
                .body(String.format("""
                        {
                          "senderAccountId": %d,
                          "receiverAccountId": %d,
                          "amount": %.2f
                        }
                        """, senderAccountId, receiverAccountId, amount))
                .post(BASE_URL + "/accounts/transfer")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body("message", Matchers.equalTo("Transfer successful"));
    }

    public static float getBalance(String token, int accountId) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("authorization", token)
                .get(BASE_URL + "/customer/accounts")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .path(String.format("find { it.id == %d }.balance", accountId));
    }


}
