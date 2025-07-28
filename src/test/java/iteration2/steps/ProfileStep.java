package iteration2.steps;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ProfileStep {
    private static final String BASE_URL = "http://localhost:4111/api/v1";

    public static void updateUserName(String token, String newName) {
        given()
                .header("authorization", token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(String.format("""
                        {
                          "name": "%s"
                        }
                        """, newName))
                .put(BASE_URL + "/customer/profile")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Profile updated successfully"));
    }

    public static String getUserName(String token) {
        return given()
                .header("authorization", token)
                .accept(ContentType.JSON)
                .get(BASE_URL + "/customer/profile")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().path("name");
    }
}
