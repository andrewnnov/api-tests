package iteration1;

import generators.RandomData;
import models.CreateUserRequestModel;
import models.UserRole;
import org.junit.jupiter.api.Test;
import requests.AdminCreateUserRequester;
import requests.CreateAccountRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class CreateAccountTest extends BaseTest {

    @Test
    public void userCanCreateAccountTest() {
        //create user
        CreateUserRequestModel createUserRequestModel = CreateUserRequestModel.builder()
                .username(RandomData.getUserName())
                .password(RandomData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        new AdminCreateUserRequester(RequestSpecs.adminSpec(),
                ResponseSpecs.entityWasCreated())
                .post(createUserRequestModel);

        new CreateAccountRequester(RequestSpecs.authAsUser(createUserRequestModel.getUsername(),
                createUserRequestModel.getPassword()), ResponseSpecs.entityWasCreated())
                .post(null);

        //check all users account and make sure that created account has in that list
    }
}
