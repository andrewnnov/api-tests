package iteration1.ui;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import api.models.CreateUserRequestModel;
import api.models.CreateUserResponseModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import api.requests.steps.AdminSteps;
import api.requests.steps.CreateModelSteps;
import ui.pages.AdminPanelPage;
import ui.pages.LoginPage;
import ui.pages.UserDashboardPage;

import java.util.Map;

import static com.codeborne.selenide.Selenide.$;

public class LoginUserTest {

    @BeforeAll
    public static void setupSelenoid() {
        Configuration.remote = "http://localhost:4444/wd/hub";
        Configuration.baseUrl = "http://192.168.100.53:3000";
        Configuration.browserSize = "1920x1080";
        Configuration.browser="chrome";
        Configuration.browserVersion = "91.0";

        Configuration.browserCapabilities.setCapability("selenoid:options",
                Map.of("enableVNC", true,
                        "enableLog", true));
    }


    @Test
    public void adminCanLoginWithCorrectDataTest() {
        CreateUserRequestModel admin = CreateUserRequestModel.getAdmin();

        new LoginPage().open().login(admin.getUsername(), admin.getPassword())
                        .getPage(AdminPanelPage.class).getAdminPanelText().shouldBe(Condition.visible);
    }

    @Test
    public void userCanLoginWithCorrectDataTest() {
        CreateUserRequestModel user = CreateModelSteps.createUserModel();
        AdminSteps.createUser(user);

        new LoginPage().open().login(user.getUsername(), user.getPassword())
                .getPage(UserDashboardPage.class)
                .getWelcomeText()
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Welcome, noname!"));
    }
}
