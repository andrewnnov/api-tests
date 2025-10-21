package iteration2.ui;

import api.helpers.AccountBalanceUtils;
import api.requests.steps.UserSteps;
import common.annotations.UserSession;
import common.storage.SessionStorage;
import io.restassured.response.ValidatableResponse;
import iteration1.ui.BaseUITest;
import org.junit.jupiter.api.Test;
import ui.pages.BankAlert;
import ui.pages.DepositMoneyPage;
import ui.pages.UserDashBoardPage;

import static org.assertj.core.api.Assertions.assertThat;


public class MakeDepositTests extends BaseUITest {

    public static final double DEPOSIT_AMOUNT = 100.00;
    public static final double INVALID_AMOUNT = 0.0;

    @Test
    @UserSession()
    public void userCanMakeDepositTest() {

        ValidatableResponse createAccountResponse = SessionStorage.getSteps().createAccount();
        long accountId = UserSteps.getAccountID(createAccountResponse);

        double accountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(SessionStorage.getUser().getUsername(),
                SessionStorage.getUser().getPassword(), accountId);

        new UserDashBoardPage().open().makeDeposit().getPage(DepositMoneyPage.class)
                .makeDeposit(accountId, DEPOSIT_AMOUNT)
                .checkAlertMessageAndAccept(BankAlert.DEPOSIT_SUCCESSFUL.getMessage() + DEPOSIT_AMOUNT + " to account ACC" + accountId + "!");

        double accountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(SessionStorage.getUser().getUsername(),
                SessionStorage.getUser().getPassword(), accountId);

        assertThat(accountBalanceBefore).isEqualTo(accountBalanceAfter - DEPOSIT_AMOUNT);
    }

    @Test
    @UserSession()
    public void userCanNotMakeDepositWithInvalidAmountTest() {

        ValidatableResponse createAccountResponse = SessionStorage.getSteps().createAccount();
        long accountId = UserSteps.getAccountID(createAccountResponse);

        double accountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(SessionStorage.getUser().getUsername(),
                SessionStorage.getUser().getPassword(), accountId);

        new UserDashBoardPage().open().makeDeposit().getPage(DepositMoneyPage.class)
                .makeDeposit(accountId, INVALID_AMOUNT).checkAlertMessageAndAccept(BankAlert.DEPOSIT_UNSUCCESSFUL.getMessage());

        double accountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(SessionStorage.getUser().getUsername(),
                SessionStorage.getUser().getPassword(), accountId);

        assertThat(accountBalanceBefore).isEqualTo(accountBalanceAfter);
    }
}
