package iteration1;

import generators.RandomData;
import generators.RandomModelGenerator;
import models.CreateAccountResponseModel;
import models.CreateUserRequestModel;
import models.CreateUserResponseModel;
import models.UserRole;
import org.junit.jupiter.api.Test;
import requests.skelethon.Endpoint;
import requests.skelethon.requesters.CrudRequester;
import requests.steps.AdminSteps;
import requests.steps.UserSteps;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class CreateAccountTest extends BaseTest {

    @Test
    public void userCanCreateAccountTest() {
        //create user
        CreateUserRequestModel createUserRequestModel =
                RandomModelGenerator.generate(CreateUserRequestModel.class);
        CreateUserResponseModel createUserResponseModel = AdminSteps.createUser(createUserRequestModel);
        UserSteps.createAccount(createUserRequestModel);

        //TODO create check account

        //check all users account and make sure that created account has in that list
    }
}
