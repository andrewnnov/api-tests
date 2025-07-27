package iteration2.tests.negative;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import iteration2.steps.AuthStep;
import iteration2.steps.ProfileStep;
import iteration2.steps.UserSteps;
import iteration2.utils.UserGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ChangeNameNegativeTest {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    private String adminToken;
    private String userToken;


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

    }

    //bug name can not be a blanc
    @Test
    public void authUserCanNotUpdateOwnNameWithBlancValue() {
        String newName = "   ";

        ProfileStep.updateUserName(userToken, newName);
        String actualName = ProfileStep.getUserName(userToken);
        Assertions.assertEquals(newName, actualName);
    }

    //bug I can inject js
    @Test
    public void userCannotInjectJavaScriptInNameField() {
        String newName = "<script>alert('XSS')</script>";

        ProfileStep.updateUserName(userToken, newName);
        String actualName = ProfileStep.getUserName(userToken);
        Assertions.assertEquals(newName, actualName);
    }


}
