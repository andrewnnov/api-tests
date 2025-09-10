package iteration1.ui;

import api.models.CreateUserRequestModel;
import api.requests.steps.AdminSteps;
import api.requests.steps.CreateModelSteps;
import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import ui.pages.AdminPanelPage;
import ui.pages.LoginPage;
import ui.pages.UserDashboard;

import static com.codeborne.selenide.Condition.visible;

public class LoginUserTest extends BaseUITest {

    @Test
    public void adminCanLoginWithCorrectDataTest() {
        CreateUserRequestModel admin = CreateUserRequestModel.getAdmin();

        new LoginPage().open().login(admin.getUsername(), admin.getPassword())
                .getPage(AdminPanelPage.class).getAdminPanelText().shouldBe(visible);
    }

    @Test
    public void userCanLoginWithCorrectDataTest() {
        CreateUserRequestModel createUserRequestModel = CreateModelSteps.createUserModel();
        AdminSteps.createUser(createUserRequestModel);

        new LoginPage().open().login(createUserRequestModel.getUsername(), createUserRequestModel.getPassword())
                .getPage(UserDashboard.class).getWelcomeText().shouldBe(visible).shouldHave(Condition.text("Welcome, noname!"));
    }
}
