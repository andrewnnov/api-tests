package iteration2.tests.positive;

import generators.RandomData;
import generators.RandomModelGenerator;
import helpers.AccountBalanceUtils;
import io.restassured.response.ValidatableResponse;
import iteration1.BaseTest;
import models.CreateUserRequestModel;
import models.MakeDepositRequestModel;
import models.MakeDepositResponseModel;
import models.UserRole;
import org.junit.jupiter.api.Test;
import requests.skelethon.Endpoint;
import requests.skelethon.requesters.CrudRequester;
import requests.steps.AdminSteps;
import requests.steps.UserSteps;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class MakeDepositTest extends BaseTest {

    @Test
    public void authUserCanDepositMoneyWithValidAmount() {
        //creating model of user
        CreateUserRequestModel createdUser = RandomModelGenerator.generate(CreateUserRequestModel.class);

        //creating user by admin
        AdminSteps.createUser(createdUser);

        //creating account
        ValidatableResponse createAccountResponse = UserSteps.createAccount(createdUser);

        //get account id
        long accountId = ((Integer) createAccountResponse.extract().path("id")).longValue();

        //creating model for deposit
        MakeDepositRequestModel makeDeposit = MakeDepositRequestModel.builder()
                .id(accountId)
                .balance(100.00).build();

        //check balances of accounts before transaction
        double accountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountId);

        //make deposit
        MakeDepositResponseModel responseModel = UserSteps.makeDeposit(createdUser, makeDeposit);

        //check balances of accounts after transaction
        double accountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountId);

        softly.assertThat(makeDeposit.getBalance()).isEqualTo(responseModel.getBalance());
        softly.assertThat(makeDeposit.getId()).isEqualTo(responseModel.getId());
        softly.assertThat(accountBalanceBefore).isEqualTo(accountBalanceAfter - 100.0);
    }


}
