package iteration2.ui;

import api.generators.RandomData;
import api.helpers.AccountBalanceUtils;
import api.models.CreateUserRequestModel;
import api.models.MakeDepositRequestModel;
import api.requests.steps.AdminSteps;
import api.requests.steps.CreateModelSteps;
import api.requests.steps.UserSteps;
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
    public void userCanMakeTransferToOtherUserTest() {

        CreateUserRequestModel userModel = CreateModelSteps.createUserModel();
        AdminSteps.createUser(userModel);

        CreateUserRequestModel userModel2 = CreateModelSteps.createUserModel();
        AdminSteps.createUser(userModel2);

        authAsUser(userModel);

        ValidatableResponse createAccountResponse = UserSteps.createAccount(userModel);
        long accountId = UserSteps.getAccountID(createAccountResponse);

        ValidatableResponse createAccountResponse2 = UserSteps.createAccount(userModel2);
        long accountIdTwo = UserSteps.getAccountID(createAccountResponse2);

        MakeDepositRequestModel makeDeposit = CreateModelSteps.createDepositModel(accountId, DEPOSIT_AMOUNT);
        UserSteps.makeDeposit(userModel, makeDeposit);

        double accountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountId);

        double accountTwoBalanceBefore = AccountBalanceUtils.getBalanceForAccount(userModel2.getUsername(),
                userModel2.getPassword(), accountIdTwo);


        new UserDashBoardPage().open().makeTransfer().getPage(MakeTransferPage.class).makeTransfer(accountId, accountIdTwo, DEPOSIT_AMOUNT)
                .checkAlertMessageAndAccept(String.format("%s%.1f to account ACC%d!",
                        BankAlert.TRANSFER_SUCCESSFUL.getMessage(),
                        DEPOSIT_AMOUNT,
                        accountIdTwo));

        double accountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountId);

        double accountTwoBalanceAfter = AccountBalanceUtils.getBalanceForAccount(userModel2.getUsername(),
                userModel2.getPassword(), accountIdTwo);

        assertThat(accountBalanceBefore).isEqualTo(accountBalanceAfter + DEPOSIT_AMOUNT);
        assertThat(accountTwoBalanceBefore).isEqualTo(accountTwoBalanceAfter - DEPOSIT_AMOUNT);
    }

    @Test
    public void userCanMakeTransferToOwnAnotherAccountTest() {

        CreateUserRequestModel userModel = CreateModelSteps.createUserModel();
        AdminSteps.createUser(userModel);

        authAsUser(userModel);

        ValidatableResponse createAccountResponse = UserSteps.createAccount(userModel);
        long accountId = UserSteps.getAccountID(createAccountResponse);

        ValidatableResponse createAccountResponse2 = UserSteps.createAccount(userModel);
        long accountIdTwo = UserSteps.getAccountID(createAccountResponse2);

        MakeDepositRequestModel makeDeposit = CreateModelSteps.createDepositModel(accountId, DEPOSIT_AMOUNT);
        UserSteps.makeDeposit(userModel, makeDeposit);

        double accountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountId);

        double accountTwoBalanceBefore = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountIdTwo);

        new UserDashBoardPage().open().makeTransfer().getPage(MakeTransferPage.class).makeTransfer(accountId, accountIdTwo, DEPOSIT_AMOUNT)
                .checkAlertMessageAndAccept(String.format("%s%.1f to account ACC%d!",
                        BankAlert.TRANSFER_SUCCESSFUL.getMessage(),
                        DEPOSIT_AMOUNT,
                        accountIdTwo));

        double accountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountId);

        double accountTwoBalanceAfter = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountIdTwo);

        assertThat(accountBalanceBefore).isEqualTo(accountBalanceAfter + DEPOSIT_AMOUNT);
        assertThat(accountTwoBalanceBefore).isEqualTo(accountTwoBalanceAfter - DEPOSIT_AMOUNT);
    }

    @Test
    public void userCanNotMakeTransferWithInValidAmount() {

        CreateUserRequestModel userModel = CreateModelSteps.createUserModel();
        AdminSteps.createUser(userModel);

        authAsUser(userModel);

        ValidatableResponse createAccountResponse = UserSteps.createAccount(userModel);
        long accountId = UserSteps.getAccountID(createAccountResponse);


        ValidatableResponse createAccountResponse2 = UserSteps.createAccount(userModel);
        long accountIdTwo = UserSteps.getAccountID(createAccountResponse2);

        MakeDepositRequestModel makeDeposit = CreateModelSteps.createDepositModel(accountId, DEPOSIT_AMOUNT);
        UserSteps.makeDeposit(userModel, makeDeposit);

        double accountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountId);

        double accountTwoBalanceBefore = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountIdTwo);

        new UserDashBoardPage().open().makeTransfer().getPage(MakeTransferPage.class).makeTransfer(accountId, accountIdTwo, INVALID_AMOUNT)
                .checkAlertMessageAndAccept(BankAlert.TRANSFER_UNSUCCESSFUL.getMessage());

        double accountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountId);

        double accountTwoBalanceAfter = AccountBalanceUtils.getBalanceForAccount(userModel.getUsername(),
                userModel.getPassword(), accountIdTwo);

        assertThat(accountBalanceBefore).isEqualTo(accountBalanceAfter);
        assertThat(accountTwoBalanceBefore).isEqualTo(accountTwoBalanceAfter);
    }
}
