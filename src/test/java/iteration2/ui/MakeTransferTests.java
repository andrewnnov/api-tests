package iteration2.ui;

import api.generators.RandomData;
import api.helpers.AccountBalanceUtils;
import api.models.MakeDepositRequestModel;
import api.requests.steps.CreateModelSteps;
import api.requests.steps.UserSteps;
import common.annotations.UserSession;
import common.storage.SessionStorage;
import io.restassured.response.ValidatableResponse;
import iteration1.ui.BaseUITest;
import org.junit.jupiter.api.Test;
import ui.pages.BankAlert;
import ui.pages.MakeTransferPage;
import ui.pages.UserDashBoardPage;

import static org.assertj.core.api.Assertions.assertThat;

public class MakeTransferTests extends BaseUITest {
    public static final double DEPOSIT_AMOUNT = RandomData.getRandomAmount();
    public static final double INVALID_AMOUNT = 0.0;
    public static final double INVALID_NEGATIVE_AMOUNT = 0.0;


    @Test
    @UserSession(value = 2, auth = 1)
    public void userCanMakeTransferToOtherUserTest() {

        ValidatableResponse createAccountResponse = SessionStorage.getSteps().createAccount();
        long accountId = UserSteps.getAccountID(createAccountResponse);

        //Create account user 2
        SessionStorage.getSteps();
        ValidatableResponse createAccountResponse2 = UserSteps.createAccount(SessionStorage.getUser(2));
        long accountIdTwo = UserSteps.getAccountID(createAccountResponse2);

        MakeDepositRequestModel makeDeposit = CreateModelSteps.createDepositModel(accountId, DEPOSIT_AMOUNT);
        UserSteps.makeDeposit(SessionStorage.getUser(1), makeDeposit);

        double accountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(SessionStorage.getUser(1).getUsername(),
                SessionStorage.getUser(1).getPassword(), accountId);

        double accountTwoBalanceBefore = AccountBalanceUtils.getBalanceForAccount(SessionStorage.getUser(2).getUsername(),
                SessionStorage.getUser(2).getPassword(), accountIdTwo);

       // authAsUser(userModel);

        new UserDashBoardPage().open().makeTransfer().getPage(MakeTransferPage.class).makeTransfer(accountId, accountIdTwo, DEPOSIT_AMOUNT)
                .checkAlertMessageAndAccept(String.format("%s%.1f to account ACC%d!",
                        BankAlert.TRANSFER_SUCCESSFUL.getMessage(),
                        DEPOSIT_AMOUNT,
                        accountIdTwo));

        double accountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(SessionStorage.getUser(1).getUsername(),
                SessionStorage.getUser(1).getPassword(), accountId);

        double accountTwoBalanceAfter = AccountBalanceUtils.getBalanceForAccount(SessionStorage.getUser(2).getUsername(),
                SessionStorage.getUser(2).getPassword(), accountIdTwo);

        assertThat(accountBalanceBefore).isEqualTo(accountBalanceAfter + DEPOSIT_AMOUNT);
        assertThat(accountTwoBalanceBefore).isEqualTo(accountTwoBalanceAfter - DEPOSIT_AMOUNT);
    }

    @Test
    @UserSession(value = 2, auth = 1)
    public void userCanMakeTransferToOwnAnotherAccountTest() {

        //Create account user 1
        ValidatableResponse createAccountResponse = SessionStorage.getSteps().createAccount();
        long accountId = UserSteps.getAccountID(createAccountResponse);

        //Create account user 2
        ValidatableResponse createAccountResponse2 = SessionStorage.getSteps().createAccount();
        long accountIdTwo = UserSteps.getAccountID(createAccountResponse2);

        MakeDepositRequestModel makeDeposit = CreateModelSteps.createDepositModel(accountId, DEPOSIT_AMOUNT);
        UserSteps.makeDeposit(SessionStorage.getUser(1), makeDeposit);

        double accountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(SessionStorage.getUser(1).getUsername(),
                SessionStorage.getUser(1).getPassword(), accountId);

        double accountTwoBalanceBefore = AccountBalanceUtils.getBalanceForAccount(SessionStorage.getUser(1).getUsername(),
                SessionStorage.getUser(1).getPassword(), accountIdTwo);

        new UserDashBoardPage().open().makeTransfer().getPage(MakeTransferPage.class).makeTransfer(accountId, accountIdTwo, DEPOSIT_AMOUNT)
                .checkAlertMessageAndAccept(String.format("%s%.1f to account ACC%d!",
                        BankAlert.TRANSFER_SUCCESSFUL.getMessage(),
                        DEPOSIT_AMOUNT,
                        accountIdTwo));

        double accountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(SessionStorage.getUser(1).getUsername(),
                SessionStorage.getUser(1).getPassword(), accountId);

        double accountTwoBalanceAfter = AccountBalanceUtils.getBalanceForAccount(SessionStorage.getUser(1).getUsername(),
                SessionStorage.getUser(1).getPassword(), accountIdTwo);

        assertThat(accountBalanceBefore).isEqualTo(accountBalanceAfter + DEPOSIT_AMOUNT);
        assertThat(accountTwoBalanceBefore).isEqualTo(accountTwoBalanceAfter - DEPOSIT_AMOUNT);
    }

    @Test
    @UserSession(value = 2, auth = 1)
    public void userCanNotMakeTransferWithInValidAmount() {


        ValidatableResponse createAccountResponse = SessionStorage.getSteps().createAccount();
        long accountId = UserSteps.getAccountID(createAccountResponse);


        ValidatableResponse createAccountResponse2 = SessionStorage.getSteps().createAccount();
        long accountIdTwo = UserSteps.getAccountID(createAccountResponse2);

        MakeDepositRequestModel makeDeposit = CreateModelSteps.createDepositModel(accountId, DEPOSIT_AMOUNT);
        UserSteps.makeDeposit(SessionStorage.getUser(1), makeDeposit);

        double accountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(SessionStorage.getUser(1).getUsername(),
                SessionStorage.getUser(1).getPassword(), accountId);

        double accountTwoBalanceBefore = AccountBalanceUtils.getBalanceForAccount(SessionStorage.getUser(1).getUsername(),
                SessionStorage.getUser(1).getPassword(), accountIdTwo);

        new UserDashBoardPage().open().makeTransfer().getPage(MakeTransferPage.class).makeTransfer(accountId, accountIdTwo, INVALID_AMOUNT)
                .checkAlertMessageAndAccept(BankAlert.TRANSFER_UNSUCCESSFUL.getMessage());

        double accountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(SessionStorage.getUser(1).getUsername(),
                SessionStorage.getUser(1).getPassword(), accountId);

        double accountTwoBalanceAfter = AccountBalanceUtils.getBalanceForAccount(SessionStorage.getUser(1).getUsername(),
                SessionStorage.getUser(1).getPassword(), accountIdTwo);

        assertThat(accountBalanceBefore).isEqualTo(accountBalanceAfter);
        assertThat(accountTwoBalanceBefore).isEqualTo(accountTwoBalanceAfter);
    }
}
