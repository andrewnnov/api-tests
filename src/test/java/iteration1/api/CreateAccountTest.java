package iteration1.api;

import api.models.CreateUserRequestModel;
import api.models.CreateUserResponseModel;
import org.junit.jupiter.api.Test;
import api.requests.steps.AdminSteps;
import api.requests.steps.CreateModelSteps;
import api.requests.steps.UserSteps;

public class CreateAccountTest extends BaseTest {

    @Test
    public void userCanCreateAccountTest() {
        CreateUserRequestModel createUserRequestModel = CreateModelSteps.createUserModel();
        CreateUserResponseModel createUserResponseModel = AdminSteps.createUser(createUserRequestModel);
        UserSteps.createAccount(createUserRequestModel);
    }
}
