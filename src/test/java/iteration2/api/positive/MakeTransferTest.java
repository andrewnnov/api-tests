package iteration2.api.positive;

import api.helpers.AccountBalanceUtils;
import io.restassured.response.ValidatableResponse;
import iteration1.api.BaseTest;
import api.models.CreateUserRequestModel;
import api.models.MakeDepositRequestModel;
import api.models.MakeTransferRequestModel;
import org.junit.jupiter.api.Test;
import api.requests.steps.AdminSteps;
import api.requests.steps.CreateModelSteps;
import api.requests.steps.UserSteps;

public class MakeTransferTest extends BaseTest {
    public static final double DEPOSIT_AMOUNT = 100.00;
    public static final double TRANSFER_AMOUNT = 50.00;

    @Test
    public void authUserCanTransferMoneyToAnotherOwnAccount() {

        CreateUserRequestModel createdUser = CreateModelSteps.createUserModel();
        AdminSteps.createUser(createdUser);

        ValidatableResponse createAccountResponseOne = UserSteps.createAccount(createdUser);
        long accountIdOne = UserSteps.getAccountID(createAccountResponseOne);

        ValidatableResponse createAccountResponseTwo = UserSteps.createAccount(createdUser);
        long accountIdTwo = UserSteps.getAccountID(createAccountResponseTwo);

        MakeDepositRequestModel makeDeposit = CreateModelSteps.createDepositModel(accountIdOne, DEPOSIT_AMOUNT);

        UserSteps.makeDeposit(createdUser, makeDeposit);

        double senderAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountIdOne);

        MakeTransferRequestModel transferRequestModel = CreateModelSteps.createTransferModel(accountIdOne, accountIdTwo, TRANSFER_AMOUNT);
        UserSteps.makeTransfer(createdUser, transferRequestModel);

        double senderAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountIdOne);

        softly.assertThat(senderAccountBalanceBefore).isEqualTo(senderAccountBalanceAfter + TRANSFER_AMOUNT);
    }

    @Test
    public void authUserCanTransferMoneyToAnotherUserAccount() {

        CreateUserRequestModel createdUser1 = CreateModelSteps.createUserModel();
        AdminSteps.createUser(createdUser1);

        CreateUserRequestModel createdUser2 = CreateModelSteps.createUserModel();
        AdminSteps.createUser(createdUser2);

        ValidatableResponse createAccountResponseOne = UserSteps.createAccount(createdUser1);
        long accountIdOne = UserSteps.getAccountID(createAccountResponseOne);

        ValidatableResponse createAccountResponseTwo = UserSteps.createAccount(createdUser2);
        long accountIdTwo = UserSteps.getAccountID(createAccountResponseTwo);

        MakeDepositRequestModel makeDeposit = CreateModelSteps.createDepositModel(accountIdOne, DEPOSIT_AMOUNT);
        UserSteps.makeDeposit(createdUser1, makeDeposit);

        MakeTransferRequestModel transferRequestModel = CreateModelSteps.createTransferModel(accountIdOne, accountIdTwo, TRANSFER_AMOUNT);

        double senderAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser1.getUsername(),
                createdUser1.getPassword(), accountIdOne);

        double receiverAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser2.getUsername(),
                createdUser2.getPassword(), accountIdTwo);

        UserSteps.makeTransfer(createdUser1, transferRequestModel);

        double senderAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser1.getUsername(),
                createdUser1.getPassword(), accountIdOne);

        double receiverAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser2.getUsername(),
                createdUser2.getPassword(), accountIdTwo);

        softly.assertThat(senderAccountBalanceBefore).isEqualTo(senderAccountBalanceAfter + TRANSFER_AMOUNT);
        softly.assertThat(receiverAccountBalanceBefore).isEqualTo(receiverAccountBalanceAfter - TRANSFER_AMOUNT);
    }
}
