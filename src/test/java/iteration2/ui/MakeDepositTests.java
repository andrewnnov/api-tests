package iteration2.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import api.helpers.AccountBalanceUtils;
import io.restassured.response.ValidatableResponse;
import api.models.CreateUserRequestModel;
import api.models.LoginUserRequestModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requesters.CrudRequester;
import api.requests.steps.AdminSteps;
import api.requests.steps.CreateModelSteps;
import api.requests.steps.UserSteps;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;

import java.util.Map;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.assertThat;


public class MakeDepositTests {

    public static final double DEPOSIT_AMOUNT = 100.00;
    public static final double INVALID_AMOUNT = 0.0;

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
    public void userCanMakeDepositTest() {
        //Step 1 Create user
        CreateUserRequestModel userModel = CreateModelSteps.createUserModel();
        AdminSteps.createUser(userModel);

        //Step 2 Get token
        String userAuthHeader = new CrudRequester(RequestSpecs.unauthSpec(),
                Endpoint.LOGIN,
                ResponseSpecs.requestReturnsOK())
                .post(LoginUserRequestModel.builder()
                        .username(userModel.getUsername())
                        .password(userModel.getPassword())
                        .build())
                .extract().header("Authorization");

        //Create account
        ValidatableResponse createAccountResponse = UserSteps.createAccount(userModel);
        long accountId = UserSteps.getAccountID(createAccountResponse);

        double accountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountId);


        //need to put this header in local storage
        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', arguments[0]);", userAuthHeader);

        Selenide.open("/dashboard");
        $(byText("\uD83D\uDCB0 Deposit Money")).click();

        $(Selectors.byText("-- Choose an account --")).click();
        $("select.account-selector").selectOptionContainingText(String.valueOf(accountId));
        $(Selectors.byAttribute("placeholder", "Enter amount")).sendKeys(String.valueOf(DEPOSIT_AMOUNT));

        $(By.xpath("//button[contains(text(),'Deposit')]")).click();

        //step 3 check that alert User created ...
        Alert alert = switchTo().alert();
        String alertText = alert.getText();

        assertThat(alertText).isEqualTo("✅ Successfully deposited $" + DEPOSIT_AMOUNT + " to account ACC" + accountId + "!");
        alert.accept();

        double accountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountId);

        assertThat(accountBalanceBefore).isEqualTo(accountBalanceAfter - DEPOSIT_AMOUNT);
    }

    @Test
    public void userCanNotMakeDepositWithInvalidAmountTest() {
        //Step 1 Create user
        CreateUserRequestModel userModel = CreateModelSteps.createUserModel();
        AdminSteps.createUser(userModel);

        //Step 2 Get token
        String userAuthHeader = new CrudRequester(RequestSpecs.unauthSpec(),
                Endpoint.LOGIN,
                ResponseSpecs.requestReturnsOK())
                .post(LoginUserRequestModel.builder()
                        .username(userModel.getUsername())
                        .password(userModel.getPassword())
                        .build())
                .extract().header("Authorization");

        //Create account
        ValidatableResponse createAccountResponse = UserSteps.createAccount(userModel);
        long accountId = UserSteps.getAccountID(createAccountResponse);

        double accountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountId);

        //need to put this header in local storage
        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', arguments[0]);", userAuthHeader);

        Selenide.open("/dashboard");
        $(byText("\uD83D\uDCB0 Deposit Money")).click();

        $(Selectors.byText("-- Choose an account --")).click();
        $("select.account-selector").selectOptionContainingText(String.valueOf(accountId));
        $(Selectors.byAttribute("placeholder", "Enter amount")).sendKeys(String.valueOf(INVALID_AMOUNT));
        $(By.xpath("//button[contains(text(),'Deposit')]")).click();

        //step 3 check that alert User created ...
        Alert alert = switchTo().alert();
        assertThat(alert.getText()).contains("❌ Please enter a valid amount.");
        alert.accept();

        double accountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountId);

        assertThat(accountBalanceBefore).isEqualTo(accountBalanceAfter);
    }
}
