package iteration2.tests.positive;

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

public class MakeDepositTest extends BaseTest {

    @Test
    public void authUserCanDepositMoneyWithValidAmount() {
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
                .balance(100.00).build();

        //check balances of accounts before transaction
        double accountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountId);

        //make deposit
        MakeDepositResponseModel responseModel = new CreateDepositRequester(RequestSpecs
                .depositAsAuthUser(createdUser.getUsername(), createdUser.getPassword()),
                ResponseSpecs.requestReturnsOK())
                .post(makeDeposit).extract().as(MakeDepositResponseModel.class);

        //check balances of accounts after transaction
        double accountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountId);

        softly.assertThat(makeDeposit.getBalance()).isEqualTo(responseModel.getBalance());
        softly.assertThat(makeDeposit.getId()).isEqualTo(responseModel.getId());
        softly.assertThat(accountBalanceBefore).isEqualTo(accountBalanceAfter - 100.0);
    }


}
