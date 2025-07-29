package iteration1;

import generators.RandomData;
import io.restassured.http.ContentType;
import models.CreateUserRequest;
import models.LoginUserRequestModel;
import models.UserRole;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import requests.AdminCreateUserRequester;
import requests.CreateAccountRequester;
import requests.LoginUserRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

import static io.restassured.RestAssured.given;

public class CreateAccountTest extends BaseTest {


    @Test
    public void userCanCreateAccountTest() {
        //create user
        CreateUserRequest userRequest = CreateUserRequest.builder()
                .username(RandomData.getUserName())
                .password(RandomData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        new AdminCreateUserRequester(
                RequestSpecs.adminSpec(),
                ResponseSpecs.entityWasCreated())
                .post(userRequest);

        //create account
        new CreateAccountRequester(RequestSpecs.authAsUser(userRequest.getUsername(),
                userRequest.getPassword()), ResponseSpecs.entityWasCreated())
                .post(null);

        //check all users account and make sure that created account has in that list
    }


}
