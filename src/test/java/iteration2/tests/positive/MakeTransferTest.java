package iteration2.tests.positive;

import generators.RandomModelGenerator;
import helpers.AccountBalanceUtils;
import io.restassured.response.ValidatableResponse;
import iteration1.BaseTest;
import models.*;
import org.junit.jupiter.api.Test;
import requests.steps.AdminSteps;
import requests.steps.UserSteps;

public class MakeTransferTest extends BaseTest {

    @Test
    public void authUserCanTransferMoneyToAnotherOwnAccount() {

        CreateUserRequestModel createdUser = RandomModelGenerator.generate(CreateUserRequestModel.class);

        AdminSteps.createUser(createdUser);

        ValidatableResponse createAccountResponseOne = UserSteps.createAccount(createdUser);

        long accountIdOne = ((Integer) createAccountResponseOne.extract().path("id")).longValue();

        ValidatableResponse createAccountResponseTwo = UserSteps.createAccount(createdUser);

        long accountIdTwo = ((Integer) createAccountResponseTwo.extract().path("id")).longValue();

        MakeDepositRequestModel makeDeposit = MakeDepositRequestModel.builder()
                .id(accountIdOne)
                .balance(100.00).build();

        MakeDepositResponseModel responseModel = UserSteps.makeDeposit(createdUser, makeDeposit);

        double senderAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountIdOne);

        MakeTransferRequestModel transferRequestModel = MakeTransferRequestModel.builder()
                .senderAccountId(accountIdOne)
                .receiverAccountId(accountIdTwo)
                .amount(50.00).build();

        MakeTransferResponseModel transferRequester = UserSteps.makeTransfer(createdUser, transferRequestModel);

        double senderAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountIdOne);

        softly.assertThat(senderAccountBalanceBefore).isEqualTo(senderAccountBalanceAfter + 50);
    }

    @Test
    public void authUserCanTransferMoneyToAnotherUserAccount() {

        CreateUserRequestModel createdUser1 = RandomModelGenerator.generate(CreateUserRequestModel.class);

        CreateUserRequestModel createdUser2 = RandomModelGenerator.generate(CreateUserRequestModel.class);

        AdminSteps.createUser(createdUser1);

        AdminSteps.createUser(createdUser2);

        ValidatableResponse createAccountResponseOne = UserSteps.createAccount(createdUser1);

        long accountIdOne = ((Integer) createAccountResponseOne.extract().path("id")).longValue();

        ValidatableResponse createAccountResponseTwo = UserSteps.createAccount(createdUser2);

        long accountIdTwo = ((Integer) createAccountResponseTwo.extract().path("id")).longValue();

        MakeDepositRequestModel makeDeposit = MakeDepositRequestModel.builder()
                .id(accountIdOne)
                .balance(100.00).build();

        MakeDepositResponseModel responseModel = UserSteps.makeDeposit(createdUser1, makeDeposit);

        MakeTransferRequestModel transferRequestModel = MakeTransferRequestModel.builder()
                .senderAccountId(accountIdOne)
                .receiverAccountId(accountIdTwo)
                .amount(50.00).build();

        double senderAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser1.getUsername(),
                createdUser1.getPassword(), accountIdOne);

        double receiverAccountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser2.getUsername(),
                createdUser2.getPassword(), accountIdTwo);

        MakeTransferResponseModel transferRequester = UserSteps.makeTransfer(createdUser1, transferRequestModel);

        double senderAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser1.getUsername(),
                createdUser1.getPassword(), accountIdOne);

        double receiverAccountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser2.getUsername(),
                createdUser2.getPassword(), accountIdTwo);

        softly.assertThat(senderAccountBalanceBefore).isEqualTo(senderAccountBalanceAfter + 50);
        softly.assertThat(receiverAccountBalanceBefore).isEqualTo(receiverAccountBalanceAfter - 50);
    }
}
