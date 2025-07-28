package iteration2;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class MakeDepositTest {

    private static String adminToken;
    private static String userToken;
    private String userId;
    private String accountId;

    @BeforeAll
    public static void setupRestAssured() {
        RestAssured.filters(List.of(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        ));

    }

    @BeforeAll
    public static void loginAsAdminAndCreateUser() {

        adminToken = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                        {
                        "username": "admin",
                        "password": "admin"
                        }
                        """)
                .post("http://localhost:4111/api/v1/auth/login")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .header("authorization", "Basic YWRtaW46YWRtaW4=").toString();

        userToken = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("authorization", adminToken)
                .body("""
                        {
                        "username": "Kat20!",
                        "password": "Kat20!",
                        "role": "USER"
                        }
                        """)
                .post("http://localhost:4111/api/v1/admin/users")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body("username", Matchers.equalTo("Kat20!"))
                .body("password", Matchers.not(Matchers.equalTo("Kat20!")))
                .body("role", Matchers.equalTo("USER")).toString();


    }



    @Test
    public void canDepositByAuthUser() {
        //auth admin
        System.out.println(userToken);
        //create user by admin

        //auth user

        //create deposit
    }



}
