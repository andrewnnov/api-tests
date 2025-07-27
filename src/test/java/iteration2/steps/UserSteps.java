package iteration2.steps;

import io.restassured.http.ContentType;
import iteration2.dto.CreateUserRequest;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;

import static io.restassured.RestAssured.given;

public class UserSteps {

    private static final String BASE_URL = "http://localhost:4111/api/v1";

    public static String createUser(String token, String username, String password) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("authorization", token)
                .body(new CreateUserRequest(username, password, "USER"))
                .post(BASE_URL + "/admin/users")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("username", Matchers.equalTo(username))
                .extract().header("authorization");
    }


}
