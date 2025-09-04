package iteration2.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import models.CreateUserRequestModel;
import models.GetUserResponseModel;
import models.LoginUserRequestModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import requests.skelethon.Endpoint;
import requests.skelethon.requesters.CrudRequester;
import requests.steps.AdminSteps;
import requests.steps.CreateModelSteps;
import requests.steps.UserSteps;
import specs.RequestSpecs;
import specs.ResponseSpecs;

import java.util.Map;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ChangeNameTests {
    private static final String NEW_USER_NAME = "Anna";

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
    public void userCanChangeOwnName() {
        CreateUserRequestModel createdUser = CreateModelSteps.createUserModel();
        AdminSteps.createUser(createdUser);

        //Step 2 Get token
        String userAuthHeader = new CrudRequester(RequestSpecs.unauthSpec(),
                Endpoint.LOGIN,
                ResponseSpecs.requestReturnsOK())
                .post(LoginUserRequestModel.builder()
                        .username(createdUser.getUsername())
                        .password(createdUser.getPassword())
                        .build())
                .extract().header("Authorization");

        //need to put this header in local storage
        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', arguments[0]);", userAuthHeader);

        Selenide.open("/edit-profile");
        $(Selectors.byAttribute("placeholder", "Enter new name")).shouldBe(visible);
        $(Selectors.byAttribute("placeholder", "Enter new name")).sendKeys(NEW_USER_NAME);
        $(Selectors.byText("\uD83D\uDCBE Save Changes")).click();

        Alert alert = switchTo().alert();
        String alertText = alert.getText();
        assertThat(alertText).isEqualTo( "✅ Name updated successfully!");
        alert.accept();

        Selenide.open("/dashboard");
        $(Selectors.byClassName("welcome-text")).shouldHave(text(NEW_USER_NAME));

        GetUserResponseModel responseModelAfter = UserSteps.getUser(createdUser);
        assertThat(responseModelAfter.getName()).isEqualTo(NEW_USER_NAME);
    }

}
