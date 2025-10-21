package iteration2.ui;

import api.models.GetUserResponseModel;
import api.requests.steps.UserSteps;
import common.annotations.UserSession;
import common.storage.SessionStorage;
import iteration1.ui.BaseUITest;
import org.junit.jupiter.api.Test;
import ui.pages.EditProfilePage;
import ui.pages.UserDashBoardPage;

import static com.codeborne.selenide.Condition.text;
import static org.assertj.core.api.Assertions.assertThat;

public class ChangeNameTests extends BaseUITest {
    private static final String NEW_USER_NAME = "Anna";

    @Test
    @UserSession()
    public void userCanChangeOwnName() {
        new EditProfilePage().open().editName(NEW_USER_NAME)
                .checkAlertMessageAndAccept("✅ Name updated successfully!")
                .getPage(UserDashBoardPage.class).open().getWelcomeText().shouldHave(text(NEW_USER_NAME));

        GetUserResponseModel responseModelAfter = UserSteps.getUser(SessionStorage.getUser());
        assertThat(responseModelAfter.getName()).isEqualTo(NEW_USER_NAME);
    }
}
