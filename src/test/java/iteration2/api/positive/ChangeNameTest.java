package iteration2.api.positive;

import iteration1.api.BaseTest;
import api.models.ChangeNameRequestModel;
import api.models.ChangeNameResponseModel;
import api.models.CreateUserRequestModel;
import api.models.GetUserResponseModel;
import org.junit.jupiter.api.Test;
import api.requests.steps.AdminSteps;
import api.requests.steps.CreateModelSteps;
import api.requests.steps.UserSteps;

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
