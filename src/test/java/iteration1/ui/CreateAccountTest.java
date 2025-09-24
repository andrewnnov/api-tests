package iteration1.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import api.models.CreateAccountResponseModel;
import api.models.CreateUserRequestModel;
import api.models.CreateUserResponseModel;
import api.models.LoginUserRequestModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requesters.CrudRequester;
import api.requests.steps.AdminSteps;
import api.requests.steps.CreateModelSteps;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


public class CreateAccountTest {
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
    public void userCanCreateAccountTest() {
        //Prepare
        //Step 1 admin login in bank
        //Step 2 admin create user
        //Step 3 User login in bank

        CreateUserRequestModel userModel = CreateModelSteps.createUserModel();
        CreateUserResponseModel user = AdminSteps.createUser(userModel);

        String userAuthHeader = new CrudRequester(RequestSpecs.unauthSpec(),
                Endpoint.LOGIN,
                ResponseSpecs.requestReturnsOK())
                .post(LoginUserRequestModel.builder()
                        .username(userModel.getUsername())
                        .password(userModel.getPassword())
                        .build())
                .extract().header("Authorization");

        //need to put this header in local storage
        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', arguments[0]);", userAuthHeader);

        //Test's step
        //Step 4 User create account
        Selenide.open("/dashboard");
        $(Selectors.byText("➕ Create New Account")).click();

        Alert alert = switchTo().alert();
        String alertText = alert.getText();
        assertThat(alertText).contains("✅ New Account Created! Account Number:");
        alert.accept();

        Pattern pattern = Pattern.compile("Account Number: (\\w+)");
        Matcher matcher = pattern.matcher(alertText);
        matcher.find();
        String createdAccNumber = matcher.group(1);

        //step 5 accont created in API
        CreateAccountResponseModel[] existingUserAccount = given()
                .spec(RequestSpecs.authAsUser(userModel.getUsername(), userModel.getPassword()))
                .get("http://localhost:4111/api/v1/customer/accounts")
                .then().assertThat().extract().as(CreateAccountResponseModel[].class);

        assertThat(existingUserAccount).hasSize(1);

        CreateAccountResponseModel createdAccount = existingUserAccount[0];

        assertThat(createdAccount).isNotNull();
        assertThat(createdAccount.getBalance()).isZero();
    }

}
