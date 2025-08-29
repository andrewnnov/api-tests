package iteration2.tests.negative;

import generators.RandomData;
import helpers.AccountBalanceUtils;
import io.restassured.response.ValidatableResponse;
import iteration1.BaseTest;
import models.*;
import org.junit.jupiter.api.Test;
import requests.skelethon.Endpoint;
import requests.skelethon.requesters.CrudRequester;
import requests.skelethon.requesters.ValidatedCrudRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class MakeTransferNegativeTest extends BaseTest {
    private static final long NOT_EXISTING_ACCOUNT_ID = 45678;

    @Test
    public void authUserCanNotTransferMoneyToOwnAccountMoreThatAccountHas() {

        //creating model of user
        CreateUserRequestModel createdUser = CreateUserRequestModel.builder()
                .username(RandomData.getUserName())
                .password(RandomData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        //creating user by admin
        new CrudRequester(RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.entityWasCreated())
                .post(createdUser);

        //creating account 1
        ValidatableResponse createAccountResponseOne = new CrudRequester(RequestSpecs
                .authAsUser(createdUser.getUsername(), createdUser.getPassword()),
                Endpoint.ACCOUNTS,
                ResponseSpecs.entityWasCreated())
                .post(null);

        //get account id
        long accountIdOne = ((Integer) createAccountResponseOne.extract().path("id")).longValue();

        //creating account 2
        ValidatableResponse createAccountResponseTwo = new CrudRequester(RequestSpecs
                .authAsUser(createdUser.getUsername(), createdUser.getPassword()),
                Endpoint.ACCOUNTS,
                ResponseSpecs.entityWasCreated())
                .post(null);

        //get account id
        long accountIdTwo = ((Integer) createAccountResponseTwo.extract().path("id")).longValue();

        MakeDepositRequestModel makeDeposit = MakeDepositRequestModel.builder()
                .id(accountIdOne)
                .balance(100.00).build();

        MakeDepositResponseModel responseModel = new ValidatedCrudRequester<MakeDepositResponseModel>(RequestSpecs
                .depositAsAuthUser(createdUser.getUsername(), createdUser.getPassword()),
                Endpoint.DEPOSIT,
                ResponseSpecs.requestReturnsOK())
                .post(makeDeposit);

        double senderAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountIdOne);

        //create transfer model
        MakeTransferRequestModel transferRequestModel = MakeTransferRequestModel.builder()
                .senderAccountId(accountIdOne)
                .receiverAccountId(accountIdTwo)
                .amount(4000.00).build();

        //create transaction
        new CrudRequester(
                RequestSpecs.authAsUser(createdUser.getUsername(), createdUser.getPassword()),
                Endpoint.TRANSFER,
                ResponseSpecs.requestReturnsBadRequest("Invalid transfer: insufficient funds or invalid accounts"))
                .post(transferRequestModel);

        //check balance of account after transaction
        double senderAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountIdOne);

        softly.assertThat(senderAccountBalanceBefore).isEqualTo(senderAccountBalanceAfter);
    }

    @Test
    public void authUserCanNotTransferMoneyToOwnAccountNegativeAmount() {
        //creating model of user
        CreateUserRequestModel createdUser = CreateUserRequestModel.builder()
                .username(RandomData.getUserName())
                .password(RandomData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        //creating user by admin
        new CrudRequester(RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.entityWasCreated())
                .post(createdUser);

        //creating account 1
        ValidatableResponse createAccountResponseOne = new CrudRequester(RequestSpecs
                .authAsUser(createdUser.getUsername(), createdUser.getPassword()),
                Endpoint.ACCOUNTS,
                ResponseSpecs.entityWasCreated())
                .post(null);

        //get account id
        long accountIdOne = ((Integer) createAccountResponseOne.extract().path("id")).longValue();

        //creating account 2
        ValidatableResponse createAccountResponseTwo = new CrudRequester(RequestSpecs
                .authAsUser(createdUser.getUsername(), createdUser.getPassword()),
                Endpoint.ACCOUNTS,
                ResponseSpecs.entityWasCreated())
                .post(null);

        //get account id
        long accountIdTwo = ((Integer) createAccountResponseTwo.extract().path("id")).longValue();

        MakeDepositRequestModel makeDeposit = MakeDepositRequestModel.builder()
                .id(accountIdOne)
                .balance(100.00).build();

        MakeDepositResponseModel responseModel = new ValidatedCrudRequester<MakeDepositResponseModel>(RequestSpecs
                .depositAsAuthUser(createdUser.getUsername(), createdUser.getPassword()),
                Endpoint.DEPOSIT,
                ResponseSpecs.requestReturnsOK())
                .post(makeDeposit);

        double senderAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountIdOne);

        //create transfer model
        MakeTransferRequestModel transferRequestModel = MakeTransferRequestModel.builder()
                .senderAccountId(accountIdOne)
                .receiverAccountId(accountIdTwo)
                .amount(-35).build();


        //create transaction
        new CrudRequester(
                RequestSpecs.authAsUser(createdUser.getUsername(), createdUser.getPassword()),
                Endpoint.TRANSFER,
                ResponseSpecs.requestReturnsBadRequest("Invalid transfer: insufficient funds or invalid accounts"))
                .post(transferRequestModel);

        //check balance of account after transaction
        double senderAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountIdOne);

        softly.assertThat(senderAccountBalanceBefore).isEqualTo(senderAccountBalanceAfter);

    }

    @Test
    public void authUserCanNotTransferMoneyToNotExistingAccount() {

        //creating model of user
        CreateUserRequestModel createdUser = CreateUserRequestModel.builder()
                .username(RandomData.getUserName())
                .password(RandomData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        //creating user by admin
        new CrudRequester(RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.entityWasCreated())
                .post(createdUser);

        //creating account 1
        ValidatableResponse createAccountResponseOne = new CrudRequester(RequestSpecs
                .authAsUser(createdUser.getUsername(), createdUser.getPassword()),
                Endpoint.ACCOUNTS,
                ResponseSpecs.entityWasCreated())
                .post(null);

        //get account id
        long accountIdOne = ((Integer) createAccountResponseOne.extract().path("id")).longValue();

        MakeDepositRequestModel makeDeposit = MakeDepositRequestModel.builder()
                .id(accountIdOne)
                .balance(100.00).build();

        MakeDepositResponseModel responseModel = new ValidatedCrudRequester<MakeDepositResponseModel>(RequestSpecs
                .depositAsAuthUser(createdUser.getUsername(), createdUser.getPassword()),
                Endpoint.DEPOSIT,
                ResponseSpecs.requestReturnsOK())
                .post(makeDeposit);

        double senderAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountIdOne);

        //create transfer model
        MakeTransferRequestModel transferRequestModel = MakeTransferRequestModel.builder()
                .senderAccountId(accountIdOne)
                .receiverAccountId(NOT_EXISTING_ACCOUNT_ID)
                .amount(20.00).build();


        //create transaction
        new CrudRequester(
                RequestSpecs.authAsUser(createdUser.getUsername(), createdUser.getPassword()),
                Endpoint.TRANSFER,
                ResponseSpecs.requestReturnsBadRequest("Invalid transfer: insufficient funds or invalid accounts"))
                .post(transferRequestModel);

        //check balance of account after transaction
        double senderAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountIdOne);

        softly.assertThat(senderAccountBalanceBefore).isEqualTo(senderAccountBalanceAfter);
    }

    @Test
    public void transferFromOtherAccountShouldBeForbidden() {
        //creating model of user1
        CreateUserRequestModel createdUser1 = CreateUserRequestModel.builder()
                .username(RandomData.getUserName())
                .password(RandomData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        //creating model of user1
        CreateUserRequestModel createdUser2 = CreateUserRequestModel.builder()
                .username(RandomData.getUserName())
                .password(RandomData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        //creating user1 by admin
        new CrudRequester(RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.entityWasCreated())
                .post(createdUser1);

        //creating user2 by admin
        new CrudRequester(RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.entityWasCreated())
                .post(createdUser2);

        //creating account 1
        ValidatableResponse createAccountResponseOne = new CrudRequester(RequestSpecs
                .authAsUser(createdUser1.getUsername(), createdUser1.getPassword()),
                Endpoint.ACCOUNTS,
                ResponseSpecs.entityWasCreated())
                .post(null);

        //get account 1 id
        long accountIdOne = ((Integer) createAccountResponseOne.extract().path("id")).longValue();

        //creating account 2
        ValidatableResponse createAccountResponseTwo = new CrudRequester(RequestSpecs
                .authAsUser(createdUser2.getUsername(), createdUser2.getPassword()),
                Endpoint.ACCOUNTS,
                ResponseSpecs.entityWasCreated())
                .post(null);

        //get account 2 id
        long accountIdTwo = ((Integer) createAccountResponseTwo.extract().path("id")).longValue();

        //make deposit account 1
        MakeDepositRequestModel makeDeposit = MakeDepositRequestModel.builder()
                .id(accountIdOne)
                .balance(100.00).build();

        //make deposit
        MakeDepositResponseModel responseModel = new ValidatedCrudRequester<MakeDepositResponseModel>(RequestSpecs
                .depositAsAuthUser(createdUser1.getUsername(), createdUser1.getPassword()),
                Endpoint.DEPOSIT,
                ResponseSpecs.requestReturnsOK())
                .post(makeDeposit);


        //create transfer model
        MakeTransferRequestModel transferRequestModel = MakeTransferRequestModel.builder()
                .senderAccountId(accountIdTwo)
                .receiverAccountId(accountIdOne)
                .amount(50.00).build();

        //check balances of accounts after transaction
        double senderAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser1.getUsername(),
                createdUser1.getPassword(), accountIdOne);

        double receiverAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser2.getUsername(),
                createdUser2.getPassword(), accountIdTwo);

        //create transaction
        new CrudRequester(
                RequestSpecs.authAsUser(createdUser1.getUsername(), createdUser1.getPassword()),
                Endpoint.TRANSFER,
                ResponseSpecs.requestReturnsForbidden("Unauthorized access to account"))
                .post(transferRequestModel);

        //check balances of accounts after transaction
        double senderAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser1.getUsername(),
                createdUser1.getPassword(), accountIdOne);

        double receiverAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser2.getUsername(),
                createdUser2.getPassword(), accountIdTwo);

        softly.assertThat(senderAccountBalanceBefore).isEqualTo(senderAccountBalanceAfter);
        softly.assertThat(receiverAccountBalanceBefore).isEqualTo(receiverAccountBalanceAfter);
    }
}
