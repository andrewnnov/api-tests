package iteration2.tests.negative;

import helpers.AccountBalanceUtils;
import io.restassured.response.ValidatableResponse;
import iteration1.BaseTest;
import models.CreateUserRequestModel;
import models.MakeDepositRequestModel;
import org.junit.jupiter.api.Test;
import requests.skelethon.Endpoint;
import requests.skelethon.requesters.CrudRequester;
import requests.steps.AdminSteps;
import requests.steps.CreateModelSteps;
import requests.steps.UserSteps;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class MakeDepositNegativeTest extends BaseTest {
    private static final long NOT_EXISTING_ACCOUNT_ID = 45678;
    public static final double DEPOSIT_AMOUNT = 100.00;
    public static final double NEGATIVE_DEPOSIT_AMOUNT = -50.00;
    public static final double ZERO_DEPOSIT_AMOUNT = 0.00;

    @Test
    public void userCannotDepositToAnotherUserAccount() {

        CreateUserRequestModel createdUser1 = CreateModelSteps.createUserModel();
        CreateUserRequestModel createdUser2 = CreateModelSteps.createUserModel();

        AdminSteps.createUser(createdUser1);
        AdminSteps.createUser(createdUser2);

        ValidatableResponse createAccountResponse1 = UserSteps.createAccount(createdUser1);
        long accountIdOne = UserSteps.getAccountID(createAccountResponse1);

        ValidatableResponse createAccountResponse2 = UserSteps.createAccount(createdUser2);
        long accountIdTwo = UserSteps.getAccountID(createAccountResponse2);

        MakeDepositRequestModel makeDeposit = CreateModelSteps.createDepositModel(accountIdTwo, DEPOSIT_AMOUNT);

        double senderAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser1.getUsername(),
                createdUser1.getPassword(), accountIdOne);

        new CrudRequester(RequestSpecs.depositAsAuthUser(
                createdUser1.getUsername(),
                createdUser1.getPassword()),
                Endpoint.DEPOSIT,
                ResponseSpecs.requestReturnsForbidden("Unauthorized access to account"))
                .post(makeDeposit);

        double senderAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser1.getUsername(),
                createdUser1.getPassword(), accountIdOne);

        softly.assertThat(senderAccountBalanceBefore).isEqualTo(senderAccountBalanceAfter);
    }


    @Test
    public void userCannotDepositToNotExistingUserAccountId() {

        CreateUserRequestModel createdUser1 = CreateModelSteps.createUserModel();
        AdminSteps.createUser(createdUser1);

        ValidatableResponse createAccountResponse1 = UserSteps.createAccount(createdUser1);
        long accountIdOne = UserSteps.getAccountID(createAccountResponse1);

        MakeDepositRequestModel makeDeposit = CreateModelSteps.createDepositModel(NOT_EXISTING_ACCOUNT_ID, DEPOSIT_AMOUNT);

        double senderAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser1.getUsername(),
                createdUser1.getPassword(), accountIdOne);

        new CrudRequester(RequestSpecs.depositAsAuthUser(
                createdUser1.getUsername(),
                createdUser1.getPassword()),
                Endpoint.DEPOSIT,
                ResponseSpecs.requestReturnsForbidden("Unauthorized access to account"))
                .post(makeDeposit);

        double senderAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser1.getUsername(),
                createdUser1.getPassword(), accountIdOne);

        softly.assertThat(senderAccountBalanceBefore).isEqualTo(senderAccountBalanceAfter);
    }


    @Test
    public void userCannotMakeNegativeDeposit() {

        CreateUserRequestModel createdUser = CreateModelSteps.createUserModel();
        AdminSteps.createUser(createdUser);

        ValidatableResponse createAccountResponse = UserSteps.createAccount(createdUser);
        long accountId = UserSteps.getAccountID(createAccountResponse);

        MakeDepositRequestModel makeDeposit = CreateModelSteps.createDepositModel(accountId, NEGATIVE_DEPOSIT_AMOUNT);

        double senderAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountId);

         new CrudRequester(RequestSpecs
                .depositAsAuthUser(createdUser.getUsername(), createdUser.getPassword()),
                Endpoint.DEPOSIT,
                ResponseSpecs.requestReturnsBadRequest("Invalid account or amount"))
                .post(makeDeposit);

        double senderAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountId);

        softly.assertThat(senderAccountBalanceBefore).isEqualTo(senderAccountBalanceAfter);
    }

    @Test
    public void userCannotMakeZeroDeposit() {
        CreateUserRequestModel createdUser = CreateModelSteps.createUserModel();
        AdminSteps.createUser(createdUser);

        ValidatableResponse createAccountResponse = UserSteps.createAccount(createdUser);
        long accountId = UserSteps.getAccountID(createAccountResponse);

        MakeDepositRequestModel makeDeposit = CreateModelSteps.createDepositModel(accountId, ZERO_DEPOSIT_AMOUNT);

        double senderAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountId);

        new CrudRequester(RequestSpecs
                .depositAsAuthUser(createdUser.getUsername(), createdUser.getPassword()),
                Endpoint.DEPOSIT,
                ResponseSpecs.requestReturnsBadRequest("Invalid account or amount"))
                .post(makeDeposit);

        double senderAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountId);

        softly.assertThat(senderAccountBalanceBefore).isEqualTo(senderAccountBalanceAfter);
    }
}
