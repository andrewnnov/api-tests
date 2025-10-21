package iteration1.ui;

import api.models.CreateUserRequestModel;
import api.requests.steps.AdminSteps;
import api.requests.steps.CreateModelSteps;
import com.codeborne.selenide.Condition;
import common.annotations.Browsers;
import org.junit.jupiter.api.Test;
import ui.pages.AdminPanelPage;
import ui.pages.LoginPage;
import ui.pages.UserDashBoardPage;

import static api.models.CreateUserRequestModel.getAdmin;

public class LoginUserTest extends BaseUITest {

    @Test
    @Browsers({"Chrome"})
    public void adminCanLoginWithCorrectDataTest() {
        CreateUserRequestModel admin = getAdmin();

        new LoginPage().open().login(admin.getUsername(), admin.getPassword())
                .getPage(AdminPanelPage.class)
                .getAdminPanelText().shouldBe(Condition.visible);

    }

    @Test
    public void userCanLoginWithCorrectDataTest() {
        CreateUserRequestModel createUserRequestModel = CreateModelSteps.createUserModel();
        AdminSteps.createUser(createUserRequestModel);

        new LoginPage().open().login(createUserRequestModel.getUsername(), createUserRequestModel.getPassword())
                .getPage(UserDashBoardPage.class)
                .getWelcomeText().shouldBe(Condition.visible).shouldHave(Condition.text("Welcome, noname!"));
    }
}
