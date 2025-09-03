package iteration2.tests.positive;

import iteration1.api.BaseTest;
import models.ChangeNameRequestModel;
import models.ChangeNameResponseModel;
import models.CreateUserRequestModel;
import models.GetUserResponseModel;
import org.junit.jupiter.api.Test;
import requests.steps.AdminSteps;
import requests.steps.CreateModelSteps;
import requests.steps.UserSteps;

public class ChangeNameTest extends BaseTest {
    private static final String NEW_USER_NAME = "Anna";

    @Test
    public void authUserCanUpdateOwnName() {

        CreateUserRequestModel createdUser = CreateModelSteps.createUserModel();
        AdminSteps.createUser(createdUser);

        ChangeNameRequestModel changeName = CreateModelSteps.changeNameModel(NEW_USER_NAME);

        ChangeNameResponseModel responseModel = UserSteps.changeName(createdUser, changeName);

        GetUserResponseModel responseModelAfter = UserSteps.getUser(createdUser);

        softly.assertThat(NEW_USER_NAME).isEqualTo(responseModel.getCustomer().getName());
        softly.assertThat(NEW_USER_NAME).isEqualTo(responseModelAfter.getName());
    }
}
