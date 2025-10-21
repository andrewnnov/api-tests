package iteration2.ui;

import api.generators.RandomData;
import api.helpers.AccountBalanceUtils;
import api.models.CreateUserRequestModel;
import api.requests.steps.AdminSteps;
import api.requests.steps.CreateModelSteps;
import api.requests.steps.UserSteps;
import io.restassured.response.ValidatableResponse;
import iteration1.ui.BaseUITest;
import org.junit.jupiter.api.Test;
import ui.pages.BankAlert;
import ui.pages.DepositMoneyPage;
import ui.pages.UserDashBoardPage;

import static org.assertj.core.api.Assertions.assertThat;


public class MakeDepositTests extends BaseUITest {

    public static final double DEPOSIT_AMOUNT = RandomData.getRandomAmount();
    public static final double INVALID_AMOUNT = 0.0;

    @Test
    public void userCanMakeDepositTest() {
        CreateUserRequestModel userModel = CreateModelSteps.createUserModel();
        AdminSteps.createUser(userModel);
        authAsUser(userModel);
        ValidatableResponse createAccountResponse = UserSteps.createAccount(userModel);
        long accountId = UserSteps.getAccountID(createAccountResponse);

        double accountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountId);

        new UserDashBoardPage().open().makeDeposit().getPage(DepositMoneyPage.class)
                .makeDeposit(accountId, DEPOSIT_AMOUNT)
                .checkAlertMessageAndAccept(String.format("%s%.1f to account ACC%d!",
                        BankAlert.DEPOSIT_SUCCESSFUL.getMessage(),
                        DEPOSIT_AMOUNT,
                        accountId));

        double accountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountId);

        assertThat(accountBalanceBefore).isEqualTo(accountBalanceAfter - DEPOSIT_AMOUNT);
    }

    @Test
    public void userCanNotMakeDepositWithInvalidAmountTest() {

        CreateUserRequestModel userModel = CreateModelSteps.createUserModel();
        AdminSteps.createUser(userModel);

        authAsUser(userModel);

        ValidatableResponse createAccountResponse = UserSteps.createAccount(userModel);
        long accountId = UserSteps.getAccountID(createAccountResponse);

        double accountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountId);

        new UserDashBoardPage().open().makeDeposit().getPage(DepositMoneyPage.class)
                .makeDeposit(accountId, INVALID_AMOUNT).checkAlertMessageAndAccept(BankAlert.DEPOSIT_UNSUCCESSFUL.getMessage());

        double accountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountId);

        assertThat(accountBalanceBefore).isEqualTo(accountBalanceAfter);
    }
}
