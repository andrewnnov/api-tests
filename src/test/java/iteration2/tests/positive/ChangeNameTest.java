package iteration2.tests.positive;

import generators.RandomData;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import iteration1.BaseTest;
import iteration2.steps.AuthStep;
import iteration2.steps.ProfileStep;
import iteration2.steps.UserSteps;
import iteration2.utils.UserGenerator;
import models.ChangeNameRequestModel;
import models.ChangeNameResponseModel;
import models.CreateUserRequestModel;
import models.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.AdminCreateUserRequester;
import requests.ChangeNameRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

import java.util.List;

public class ChangeNameTest extends BaseTest {
    private String newUserName = "Anna";



    @Test
    public void authUserCanUpdateOwnName() {

        CreateUserRequestModel createdUser = CreateUserRequestModel.builder()
                .username(RandomData.getUserName())
                .password(RandomData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        new AdminCreateUserRequester(RequestSpecs.adminSpec(), ResponseSpecs.entityWasCreated())
                .post(createdUser);

        ChangeNameRequestModel changeName = ChangeNameRequestModel.builder()
                .name(newUserName).build();

        ChangeNameResponseModel responseModel = new ChangeNameRequester(RequestSpecs.authAsUser(createdUser.getUsername(),
                createdUser.getPassword()), ResponseSpecs.requestReturnOK("Profile updated successfully"))
                .put(changeName).extract().as(ChangeNameResponseModel.class);

        softly.assertThat(newUserName).isEqualTo(responseModel.getCustomer().getName());
    }
}
