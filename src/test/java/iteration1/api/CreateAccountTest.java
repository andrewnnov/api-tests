package iteration1.api;

import models.CreateUserRequestModel;
import models.CreateUserResponseModel;
import org.junit.jupiter.api.Test;
import requests.steps.AdminSteps;
import requests.steps.CreateModelSteps;
import requests.steps.UserSteps;

public class CreateAccountTest extends BaseTest {

    @Test
    public void userCanCreateAccountTest() {
        CreateUserRequestModel createUserRequestModel = CreateModelSteps.createUserModel();
        CreateUserResponseModel createUserResponseModel = AdminSteps.createUser(createUserRequestModel);
        UserSteps.createAccount(createUserRequestModel);
    }
}
