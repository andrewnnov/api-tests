package iteration2.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import api.helpers.AccountBalanceUtils;
import io.restassured.response.ValidatableResponse;
import api.models.CreateUserRequestModel;
import api.models.LoginUserRequestModel;
import api.models.MakeDepositRequestModel;
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

public class MakeTransferTests {
    public static final double DEPOSIT_AMOUNT = 100.00;
    public static final double INVALID_AMOUNT = 0.0;
    public static final double INVALID_NEGATIVE_AMOUNT = 0.0;

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
    public void userCanMakeTransferToOtherUserTest() {

        //Step 1 Create user
        CreateUserRequestModel userModel = CreateModelSteps.createUserModel();
        AdminSteps.createUser(userModel);

        //Step 1 Create user 2
        CreateUserRequestModel userModel2 = CreateModelSteps.createUserModel();
        AdminSteps.createUser(userModel2);

        //Step 2 Get token
        String userAuthHeader = new CrudRequester(RequestSpecs.unauthSpec(),
                Endpoint.LOGIN,
                ResponseSpecs.requestReturnsOK())
                .post(LoginUserRequestModel.builder()
                        .username(userModel.getUsername())
                        .password(userModel.getPassword())
                        .build())
                .extract().header("Authorization");

        //Create account user 1
        ValidatableResponse createAccountResponse = UserSteps.createAccount(userModel);
        long accountId = UserSteps.getAccountID(createAccountResponse);

        //Create account user 2
        ValidatableResponse createAccountResponse2 = UserSteps.createAccount(userModel2);
        long accountIdTwo = UserSteps.getAccountID(createAccountResponse2);

        MakeDepositRequestModel makeDeposit = CreateModelSteps.createDepositModel(accountId, DEPOSIT_AMOUNT);
        UserSteps.makeDeposit(userModel, makeDeposit);

        double accountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountId);

        double accountTwoBalanceBefore = AccountBalanceUtils.getBalanceForAccount(userModel2.getUsername(),
                userModel2.getPassword(), accountIdTwo);

        //need to put this header in local storage
        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', arguments[0]);", userAuthHeader);

        Selenide.open("/dashboard");
        $(byText("\uD83D\uDD04 Make a Transfer")).click();

        $(Selectors.byText("-- Choose an account --")).click();
        $("select.account-selector").selectOptionContainingText(String.valueOf(accountId));

        $(Selectors.byAttribute("placeholder", "Enter recipient account number")).sendKeys("ACC" + accountIdTwo);
        $(Selectors.byAttribute("placeholder", "Enter amount")).sendKeys(String.valueOf(DEPOSIT_AMOUNT));
        $(Selectors.byId("confirmCheck")).click();
        $(By.xpath("//button[contains(text(),'Send Transfer')]")).click();

        //step 3 check that alert User created ...
        Alert alert = switchTo().alert();
        String alertText = alert.getText();
        assertThat(alertText).isEqualTo("✅ Successfully transferred $" + DEPOSIT_AMOUNT + " to account ACC" + accountIdTwo + "!");
        alert.accept();

        double accountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountId);

        double accountTwoBalanceAfter = AccountBalanceUtils.getBalanceForAccount(userModel2.getUsername(),
                userModel2.getPassword(), accountIdTwo);

        assertThat(accountBalanceBefore).isEqualTo(accountBalanceAfter + DEPOSIT_AMOUNT);
        assertThat(accountTwoBalanceBefore).isEqualTo(accountTwoBalanceAfter - DEPOSIT_AMOUNT);
    }

    @Test
    public void userCanMakeTransferToOwnAnotherAccountTest() {

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

        //Create account user 1
        ValidatableResponse createAccountResponse = UserSteps.createAccount(userModel);
        long accountId = UserSteps.getAccountID(createAccountResponse);

        //Create account user 2
        ValidatableResponse createAccountResponse2 = UserSteps.createAccount(userModel);
        long accountIdTwo = UserSteps.getAccountID(createAccountResponse2);

        MakeDepositRequestModel makeDeposit = CreateModelSteps.createDepositModel(accountId, DEPOSIT_AMOUNT);
        UserSteps.makeDeposit(userModel, makeDeposit);

        double accountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountId);

        double accountTwoBalanceBefore = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountIdTwo);

        //need to put this header in local storage
        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', arguments[0]);", userAuthHeader);

        Selenide.open("/dashboard");
        $(byText("\uD83D\uDD04 Make a Transfer")).click();

        $(Selectors.byText("-- Choose an account --")).click();
        $("select.account-selector").selectOptionContainingText(String.valueOf(accountId));

        $(Selectors.byAttribute("placeholder", "Enter recipient account number")).sendKeys("ACC" + accountIdTwo);
        $(Selectors.byAttribute("placeholder", "Enter amount")).sendKeys(String.valueOf(DEPOSIT_AMOUNT));
        $(Selectors.byId("confirmCheck")).click();
        $(By.xpath("//button[contains(text(),'Send Transfer')]")).click();

        //step 3 check that alert User created ...
        Alert alert = switchTo().alert();
        String alertText = alert.getText();

        assertThat(alertText).isEqualTo("✅ Successfully transferred $" + DEPOSIT_AMOUNT + " to account ACC" + accountIdTwo + "!");
        alert.accept();

        double accountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountId);

        double accountTwoBalanceAfter = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountIdTwo);

        assertThat(accountBalanceBefore).isEqualTo(accountBalanceAfter + DEPOSIT_AMOUNT);
        assertThat(accountTwoBalanceBefore).isEqualTo(accountTwoBalanceAfter - DEPOSIT_AMOUNT);
    }

    @Test
    public void userCanNotMakeTransferWithInValidAmount() {

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

        //Create account user 1
        ValidatableResponse createAccountResponse = UserSteps.createAccount(userModel);
        long accountId = UserSteps.getAccountID(createAccountResponse);

        //Create account user 2
        ValidatableResponse createAccountResponse2 = UserSteps.createAccount(userModel);
        long accountIdTwo = UserSteps.getAccountID(createAccountResponse2);

        MakeDepositRequestModel makeDeposit = CreateModelSteps.createDepositModel(accountId, DEPOSIT_AMOUNT);
        UserSteps.makeDeposit(userModel, makeDeposit);

        double accountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountId);

        double accountTwoBalanceBefore = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountIdTwo);


        //need to put this header in local storage
        Selenide.open("/");
        executeJavaScript("localStorage.setItem('authToken', arguments[0]);", userAuthHeader);

        Selenide.open("/dashboard");
        $(byText("\uD83D\uDD04 Make a Transfer")).click();

        $(Selectors.byText("-- Choose an account --")).click();
        $("select.account-selector").selectOptionContainingText(String.valueOf(accountId));

        $(Selectors.byAttribute("placeholder", "Enter recipient account number")).sendKeys("ACC" + accountIdTwo);
        $(Selectors.byAttribute("placeholder", "Enter amount")).sendKeys(String.valueOf(INVALID_NEGATIVE_AMOUNT));
        $(Selectors.byId("confirmCheck")).click();
        $(By.xpath("//button[contains(text(),'Send Transfer')]")).click();

        //step 3 check that alert User created ...
        Alert alert = switchTo().alert();
        String alertText = alert.getText();

        assertThat(alertText).isEqualTo("❌ Error: Invalid transfer: insufficient funds or invalid accounts");
        alert.accept();

        double accountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountId);

        double accountTwoBalanceAfter = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountIdTwo);

        assertThat(accountBalanceBefore).isEqualTo(accountBalanceAfter);
        assertThat(accountTwoBalanceBefore).isEqualTo(accountTwoBalanceAfter);
    }
}
