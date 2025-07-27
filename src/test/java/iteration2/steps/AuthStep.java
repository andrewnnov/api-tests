package iteration2.steps;

import io.restassured.http.ContentType;
import iteration2.dto.LoginRequest;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class AuthStep {
    private static final String BASE_URL = "http://localhost:4111/api/v1";

    public static String login(String userName, String password) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(new LoginRequest(userName, password))
                .post(BASE_URL + "/auth/login")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().header("authorization");
    }

}
