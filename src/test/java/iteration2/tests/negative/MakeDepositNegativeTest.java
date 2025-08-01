package iteration2.tests.negative;

import generators.RandomData;
import helpers.AccountBalanceUtils;
import io.restassured.response.ValidatableResponse;
import iteration1.BaseTest;
import models.CreateUserRequestModel;
import models.MakeDepositRequestModel;
import models.MakeDepositResponseModel;
import models.UserRole;
import org.junit.jupiter.api.Test;
import requests.AdminCreateUserRequester;
import requests.CreateAccountRequester;
import requests.CreateDepositRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class MakeDepositNegativeTest extends BaseTest {
    private static final long NOT_EXISTING_ACCOUNT_ID = 45678;

    @Test
    public void userCannotDepositToAnotherUserAccount() {

        CreateUserRequestModel createdUser1 = CreateUserRequestModel.builder()
                .username(RandomData.getUserName())
                .password(RandomData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        CreateUserRequestModel createdUser2 = CreateUserRequestModel.builder()
                .username(RandomData.getUserName())
                .password(RandomData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        new AdminCreateUserRequester(RequestSpecs.adminSpec(), ResponseSpecs.entityWasCreated())
                .post(createdUser1);

        new AdminCreateUserRequester(RequestSpecs.adminSpec(), ResponseSpecs.entityWasCreated())
                .post(createdUser2);

        ValidatableResponse createAccountResponse1 = new CreateAccountRequester(
                RequestSpecs.authAsUser(createdUser1.getUsername(), createdUser1.getPassword()),
                ResponseSpecs.entityWasCreated())
                .post(null);

        long accountIdOne = ((Integer) createAccountResponse1.extract().path("id")).longValue();

        ValidatableResponse createAccountResponse2 = new CreateAccountRequester(
                RequestSpecs.authAsUser(createdUser2.getUsername(), createdUser2.getPassword()),
                ResponseSpecs.entityWasCreated())
                .post(null);

        long accountIdTwo = ((Integer) createAccountResponse2.extract().path("id")).longValue();

        MakeDepositResponseModel makeDeposit = MakeDepositResponseModel.builder()
                .id(accountIdTwo)
                .balance(100.00)
                .build();

        double senderAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser1.getUsername(),
                createdUser1.getPassword(), accountIdOne);

        new CreateDepositRequester(RequestSpecs.depositAsAuthUser(
                createdUser1.getUsername(),
                createdUser1.getPassword()),
                ResponseSpecs.requestReturnsForbidden("Unauthorized access to account"))
                .post(makeDeposit);

        double senderAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser1.getUsername(),
                createdUser1.getPassword(), accountIdOne);

        softly.assertThat(senderAccountBalanceBefore).isEqualTo(senderAccountBalanceAfter);
    }


    @Test
    public void userCannotDepositToNotExistingUserAccountId() {

        CreateUserRequestModel createdUser1 = CreateUserRequestModel.builder()
                .username(RandomData.getUserName())
                .password(RandomData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        new AdminCreateUserRequester(RequestSpecs.adminSpec(), ResponseSpecs.entityWasCreated())
                .post(createdUser1);

        ValidatableResponse createAccountResponse1 = new CreateAccountRequester(
                RequestSpecs.authAsUser(createdUser1.getUsername(), createdUser1.getPassword()),
                ResponseSpecs.entityWasCreated())
                .post(null);

        long accountIdOne = ((Integer) createAccountResponse1.extract().path("id")).longValue();

        MakeDepositResponseModel makeDeposit = MakeDepositResponseModel.builder()
                .id(NOT_EXISTING_ACCOUNT_ID)
                .balance(100.00)
                .build();

        double senderAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser1.getUsername(),
                createdUser1.getPassword(), accountIdOne);

        new CreateDepositRequester(RequestSpecs.depositAsAuthUser(
                createdUser1.getUsername(),
                createdUser1.getPassword()),
                ResponseSpecs.requestReturnsForbidden("Unauthorized access to account"))
                .post(makeDeposit);

        double senderAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser1.getUsername(),
                createdUser1.getPassword(), accountIdOne);

        softly.assertThat(senderAccountBalanceBefore).isEqualTo(senderAccountBalanceAfter);
    }


    @Test
    public void userCannotMakeNegativeDeposit() {

        //creating model of user
        CreateUserRequestModel createdUser = CreateUserRequestModel.builder()
                .username(RandomData.getUserName())
                .password(RandomData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        //creating user by admin
        new AdminCreateUserRequester(RequestSpecs.adminSpec(), ResponseSpecs.entityWasCreated())
                .post(createdUser);

        //creating account
        ValidatableResponse createAccountResponse = new CreateAccountRequester(RequestSpecs
                .authAsUser(createdUser.getUsername(), createdUser.getPassword()),
                ResponseSpecs.entityWasCreated())
                .post(null);

        //get account id
        long accountId = ((Integer) createAccountResponse.extract().path("id")).longValue();

        //creating model for deposit
        MakeDepositRequestModel makeDeposit = MakeDepositRequestModel.builder()
                .id(accountId)
                .balance(-50.00).build();

        double senderAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountId);

        //make deposit
         new CreateDepositRequester(RequestSpecs
                .depositAsAuthUser(createdUser.getUsername(), createdUser.getPassword()),
                ResponseSpecs.requestReturnsBadRequest("Invalid account or amount"))
                .post(makeDeposit);

        double senderAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountId);
    }


    @Test
    public void userCannotMakeZeroDeposit() {
        CreateUserRequestModel createdUser = CreateUserRequestModel.builder()
                .username(RandomData.getUserName())
                .password(RandomData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        //creating user by admin
        new AdminCreateUserRequester(RequestSpecs.adminSpec(), ResponseSpecs.entityWasCreated())
                .post(createdUser);

        //creating account
        ValidatableResponse createAccountResponse = new CreateAccountRequester(RequestSpecs
                .authAsUser(createdUser.getUsername(), createdUser.getPassword()),
                ResponseSpecs.entityWasCreated())
                .post(null);

        //get account id
        long accountId = ((Integer) createAccountResponse.extract().path("id")).longValue();

        //creating model for deposit
        MakeDepositRequestModel makeDeposit = MakeDepositRequestModel.builder()
                .id(accountId)
                .balance(0.00).build();

        double senderAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountId);

        //make deposit
        new CreateDepositRequester(RequestSpecs
                .depositAsAuthUser(createdUser.getUsername(), createdUser.getPassword()),
                ResponseSpecs.requestReturnsBadRequest("Invalid account or amount"))
                .post(makeDeposit);

        double senderAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountId);

        softly.assertThat(senderAccountBalanceBefore).isEqualTo(senderAccountBalanceAfter);
    }
}
