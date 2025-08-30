package iteration2.tests.positive;

import generators.RandomData;
import generators.RandomModelGenerator;
import helpers.AccountBalanceUtils;
import io.restassured.response.ValidatableResponse;
import iteration1.BaseTest;
import models.*;
import org.junit.jupiter.api.Test;
import requests.skelethon.Endpoint;
import requests.skelethon.requesters.CrudRequester;
import requests.skelethon.requesters.ValidatedCrudRequester;
import requests.steps.AdminSteps;
import requests.steps.UserSteps;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class MakeTransferTest extends BaseTest {

    @Test
    public void authUserCanTransferMoneyToAnotherOwnAccount() {

        //creating model of user
        CreateUserRequestModel createdUser = RandomModelGenerator.generate(CreateUserRequestModel.class);

        //creating user by admin
        AdminSteps.createUser(createdUser);

        //creating account 1
        ValidatableResponse createAccountResponseOne = UserSteps.createAccount(createdUser);

        //get account id
        long accountIdOne = ((Integer) createAccountResponseOne.extract().path("id")).longValue();

        //creating account 2
        ValidatableResponse createAccountResponseTwo = UserSteps.createAccount(createdUser);

        //get account id
        long accountIdTwo = ((Integer) createAccountResponseTwo.extract().path("id")).longValue();

        MakeDepositRequestModel makeDeposit = MakeDepositRequestModel.builder()
                .id(accountIdOne)
                .balance(100.00).build();

        //make deposit
        MakeDepositResponseModel responseModel = UserSteps.makeDeposit(createdUser, makeDeposit);


        //check balance of account before transaction
        double senderAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountIdOne);

        //create transfer model
        MakeTransferRequestModel transferRequestModel = MakeTransferRequestModel.builder()
                .senderAccountId(accountIdOne)
                .receiverAccountId(accountIdTwo)
                .amount(50.00).build();

        //create transaction
        MakeTransferResponseModel transferRequester = UserSteps.makeTransfer(createdUser, transferRequestModel);

        //check balance of account after transaction
        double senderAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountIdOne);

        softly.assertThat(senderAccountBalanceBefore).isEqualTo(senderAccountBalanceAfter + 50);
    }

    @Test
    public void authUserCanTransferMoneyToAnotherUserAccount() {

        //creating model of user1
        CreateUserRequestModel createdUser1 = RandomModelGenerator.generate(CreateUserRequestModel.class);

        //creating model of user1
        CreateUserRequestModel createdUser2 = RandomModelGenerator.generate(CreateUserRequestModel.class);

        //creating user1 by admin
        AdminSteps.createUser(createdUser1);

        //creating user2 by admin
        AdminSteps.createUser(createdUser2);

        //creating account 1
        ValidatableResponse createAccountResponseOne = UserSteps.createAccount(createdUser1);

        //get account 1 id
        long accountIdOne = ((Integer) createAccountResponseOne.extract().path("id")).longValue();

        //creating account 2
        ValidatableResponse createAccountResponseTwo = UserSteps.createAccount(createdUser2);

        //get account 2 id
        long accountIdTwo = ((Integer) createAccountResponseTwo.extract().path("id")).longValue();

        //make deposit account 1
        MakeDepositRequestModel makeDeposit = MakeDepositRequestModel.builder()
                .id(accountIdOne)
                .balance(100.00).build();

        //make deposit
        MakeDepositResponseModel responseModel = UserSteps.makeDeposit(createdUser1, makeDeposit);


        //create transfer model
        MakeTransferRequestModel transferRequestModel = MakeTransferRequestModel.builder()
                .senderAccountId(accountIdOne)
                .receiverAccountId(accountIdTwo)
                .amount(50.00).build();

        //check balances of accounts before transaction
        double senderAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser1.getUsername(),
                createdUser1.getPassword(), accountIdOne);

        double receiverAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser2.getUsername(),
                createdUser2.getPassword(), accountIdTwo);

        //create transaction
        MakeTransferResponseModel transferRequester = UserSteps.makeTransfer(createdUser1, transferRequestModel);

        //check balances of accounts after transaction
        double senderAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser1.getUsername(),
                createdUser1.getPassword(), accountIdOne);

        double receiverAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser2.getUsername(),
                createdUser2.getPassword(), accountIdTwo);

        softly.assertThat(senderAccountBalanceBefore).isEqualTo(senderAccountBalanceAfter + 50);
        softly.assertThat(receiverAccountBalanceBefore).isEqualTo(receiverAccountBalanceAfter - 50);
    }
}
