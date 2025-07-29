package iteration1;

import generators.RandomData;
import models.CreateUserRequest;
import models.LoginUserRequestModel;
import models.UserRole;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import requests.AdminCreateUserRequester;
import requests.LoginUserRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class LoginUserTest extends BaseTest {

    @Test
    public void adminCanGenerateAuthTokenTest() {

        LoginUserRequestModel userRequestModel = LoginUserRequestModel.builder()
                .username("admin")
                .password("admin")
                .build();

        new LoginUserRequester(
                RequestSpecs.unAuthSpec(),
                ResponseSpecs.requestReturnOk())
                .post(userRequestModel);
    }

    @Test
    public void userCanGenerateAuthTokenTest() {
        //create user
        CreateUserRequest userRequest = CreateUserRequest.builder()
                .username(RandomData.getUserName())
                .password(RandomData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        //create User
        new AdminCreateUserRequester(
                RequestSpecs.adminSpec(),
                ResponseSpecs.entityWasCreated())
                .post(userRequest);


        new LoginUserRequester(RequestSpecs.unAuthSpec(),
                ResponseSpecs.requestReturnOk())
                .post(LoginUserRequestModel.builder()
                        .username(userRequest.getUsername())
                        .password(userRequest.getPassword())
                        .build())
                .header("authorization", Matchers.notNullValue());

    }

}
