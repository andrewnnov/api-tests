package iteration2.api.negative;

import helpers.AccountBalanceUtils;
import io.restassured.response.ValidatableResponse;
import iteration1.api.BaseTest;
import models.CreateUserRequestModel;
import models.MakeDepositRequestModel;
import models.MakeDepositResponseModel;
import models.MakeTransferRequestModel;
import org.junit.jupiter.api.Test;
import requests.skelethon.Endpoint;
import requests.skelethon.requesters.CrudRequester;
import requests.steps.AdminSteps;
import requests.steps.CreateModelSteps;
import requests.steps.UserSteps;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class MakeTransferNegativeTest extends BaseTest {
    private static final long NOT_EXISTING_ACCOUNT_ID = 45678;
    private static final double DEPOSIT_AMOUNT = 100.00;
    private static final double AMOUNT_MORE_THAN_ACCOUNT_HAS = 4000.00;
    private static final double NEGATIVE_AMOUNT = -35.00;



    @Test
    public void authUserCanNotTransferMoneyToOwnAccountMoreThatAccountHas() {

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

        MakeTransferRequestModel transferRequestModel = CreateModelSteps.createTransferModel(accountIdOne, accountIdTwo, AMOUNT_MORE_THAN_ACCOUNT_HAS);

        new CrudRequester(
                RequestSpecs.authAsUser(createdUser.getUsername(), createdUser.getPassword()),
                Endpoint.TRANSFER,
                ResponseSpecs.requestReturnsBadRequest("Invalid transfer: insufficient funds or invalid accounts"))
                .post(transferRequestModel);

        double senderAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountIdOne);

        softly.assertThat(senderAccountBalanceBefore).isEqualTo(senderAccountBalanceAfter);
    }

    @Test
    public void authUserCanNotTransferMoneyToOwnAccountNegativeAmount() {

        CreateUserRequestModel createdUser = CreateModelSteps.createUserModel();
        AdminSteps.createUser(createdUser);

        ValidatableResponse createAccountResponseOne = UserSteps.createAccount(createdUser);
        long accountIdOne = UserSteps.getAccountID(createAccountResponseOne);

        ValidatableResponse createAccountResponseTwo = UserSteps.createAccount(createdUser);
        long accountIdTwo = UserSteps.getAccountID(createAccountResponseTwo);

        MakeDepositRequestModel makeDeposit = CreateModelSteps.createDepositModel(accountIdOne, DEPOSIT_AMOUNT);

        MakeDepositResponseModel responseModel = UserSteps.makeDeposit(createdUser, makeDeposit);

        double senderAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountIdOne);

        MakeTransferRequestModel transferRequestModel = CreateModelSteps.createTransferModel(accountIdOne, accountIdTwo, NEGATIVE_AMOUNT);

        new CrudRequester(
                RequestSpecs.authAsUser(createdUser.getUsername(), createdUser.getPassword()),
                Endpoint.TRANSFER,
                ResponseSpecs.requestReturnsBadRequest("Invalid transfer: insufficient funds or invalid accounts"))
                .post(transferRequestModel);

        double senderAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountIdOne);

        softly.assertThat(senderAccountBalanceBefore).isEqualTo(senderAccountBalanceAfter);
    }

    @Test
    public void authUserCanNotTransferMoneyToNotExistingAccount() {

        CreateUserRequestModel createdUser = CreateModelSteps.createUserModel();
        AdminSteps.createUser(createdUser);

        ValidatableResponse createAccountResponseOne = UserSteps.createAccount(createdUser);
        long accountIdOne = UserSteps.getAccountID(createAccountResponseOne);

        MakeDepositRequestModel makeDeposit = CreateModelSteps.createDepositModel(accountIdOne, DEPOSIT_AMOUNT);
        UserSteps.makeDeposit(createdUser, makeDeposit);

        double senderAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountIdOne);

        MakeTransferRequestModel transferRequestModel = CreateModelSteps.createTransferModel(accountIdOne, NOT_EXISTING_ACCOUNT_ID, DEPOSIT_AMOUNT);

        new CrudRequester(
                RequestSpecs.authAsUser(createdUser.getUsername(), createdUser.getPassword()),
                Endpoint.TRANSFER,
                ResponseSpecs.requestReturnsBadRequest("Invalid transfer: insufficient funds or invalid accounts"))
                .post(transferRequestModel);

        double senderAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountIdOne);

        softly.assertThat(senderAccountBalanceBefore).isEqualTo(senderAccountBalanceAfter);
    }

    @Test
    public void transferFromOtherAccountShouldBeForbidden() {

        CreateUserRequestModel createdUser1 = CreateModelSteps.createUserModel();
        AdminSteps.createUser(createdUser1);

        CreateUserRequestModel createdUser2 = CreateModelSteps.createUserModel();
        AdminSteps.createUser(createdUser2);

        ValidatableResponse createAccountResponseOne = UserSteps.createAccount(createdUser1);
        long accountIdOne = UserSteps.getAccountID(createAccountResponseOne);

        ValidatableResponse createAccountResponseTwo = UserSteps.createAccount(createdUser2);
        long accountIdTwo = UserSteps.getAccountID(createAccountResponseTwo);

        MakeDepositRequestModel makeDeposit = CreateModelSteps.createDepositModel(accountIdOne, DEPOSIT_AMOUNT);

        MakeDepositResponseModel responseModel = UserSteps.makeDeposit(createdUser1, makeDeposit);

        MakeTransferRequestModel transferRequestModel = CreateModelSteps.createTransferModel(accountIdTwo, accountIdOne, DEPOSIT_AMOUNT);

        double senderAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser1.getUsername(),
                createdUser1.getPassword(), accountIdOne);

        double receiverAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser2.getUsername(),
                createdUser2.getPassword(), accountIdTwo);

        new CrudRequester(
                RequestSpecs.authAsUser(createdUser1.getUsername(), createdUser1.getPassword()),
                Endpoint.TRANSFER,
                ResponseSpecs.requestReturnsForbidden("Unauthorized access to account"))
                .post(transferRequestModel);

        double senderAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser1.getUsername(),
                createdUser1.getPassword(), accountIdOne);

        double receiverAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser2.getUsername(),
                createdUser2.getPassword(), accountIdTwo);

        softly.assertThat(senderAccountBalanceBefore).isEqualTo(senderAccountBalanceAfter);
        softly.assertThat(receiverAccountBalanceBefore).isEqualTo(receiverAccountBalanceAfter);
    }
}
