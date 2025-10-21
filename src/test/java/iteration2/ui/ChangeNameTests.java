package iteration2.ui;

import api.models.CreateUserRequestModel;
import api.models.GetUserResponseModel;
import api.requests.steps.AdminSteps;
import api.requests.steps.CreateModelSteps;
import api.requests.steps.UserSteps;
import iteration1.ui.BaseUITest;
import org.junit.jupiter.api.Test;
import ui.pages.EditProfilePage;
import ui.pages.UserDashBoardPage;

import static com.codeborne.selenide.Condition.text;
import static org.assertj.core.api.Assertions.assertThat;

public class ChangeNameTests extends BaseUITest {
    private static final String NEW_USER_NAME = "Anna";

    @Test
    public void userCanChangeOwnName() {
        CreateUserRequestModel userModel = CreateModelSteps.createUserModel();
        AdminSteps.createUser(userModel);
        authAsUser(userModel);

        new EditProfilePage().open().editName(NEW_USER_NAME)
                .checkAlertMessageAndAccept(EditProfilePage.NAME_UPDATE_SUCCESSFULLY)
                .getPage(UserDashBoardPage.class).open().getWelcomeText().shouldHave(text(NEW_USER_NAME));

        GetUserResponseModel responseModelAfter = UserSteps.getUser(userModel);
        assertThat(responseModelAfter.getName()).isEqualTo(NEW_USER_NAME);
    }
}
