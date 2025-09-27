package iteration1.ui;

import api.models.CreateUserRequestModel;
import api.models.CreateUserResponseModel;
import api.requests.steps.AdminSteps;
import api.requests.steps.CreateModelSteps;
import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import ui.pages.AdminPanelPage;
import ui.pages.LoginPage;
import ui.pages.UserDashBoardPage;

public class LoginUserTest extends BaseUiTest {


    @Test
    public void adminCanLoginWithCorrectDataTest() {
        CreateUserRequestModel admin = CreateUserRequestModel.getAdmin();

        new LoginPage().open().login(admin.getUsername(), admin.getPassword())
                        .getPage(AdminPanelPage.class).getAdminPanelText().shouldBe(Condition.visible);


    }

    @Test
    public void userCanLoginWithCorrectDataTest() {
        CreateUserRequestModel createUserRequestModel = CreateModelSteps.createUserModel();
        CreateUserResponseModel user = AdminSteps.createUser(createUserRequestModel);

        new LoginPage().open().login(createUserRequestModel.getUsername(), createUserRequestModel.getPassword())
                        .getPage(UserDashBoardPage.class).getWelcomeText()
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Welcome, noname!"));
    }
}
