package iteration2.api.positive;

import api.helpers.AccountBalanceUtils;
import io.restassured.response.ValidatableResponse;
import iteration1.api.BaseTest;
import api.models.CreateUserRequestModel;
import api.models.MakeDepositRequestModel;
import api.models.MakeDepositResponseModel;
import org.junit.jupiter.api.Test;
import api.requests.steps.AdminSteps;
import api.requests.steps.CreateModelSteps;
import api.requests.steps.UserSteps;

public class MakeDepositTest extends BaseTest {
    public static final double DEPOSIT_AMOUNT = 100.00;

    @Test
    public void authUserCanDepositMoneyWithValidAmount() {

        CreateUserRequestModel createdUser = CreateModelSteps.createUserModel();
        AdminSteps.createUser(createdUser);

        ValidatableResponse createAccountResponse = UserSteps.createAccount(createdUser);
        long accountId = UserSteps.getAccountID(createAccountResponse);

        MakeDepositRequestModel makeDeposit = CreateModelSteps.createDepositModel(accountId, DEPOSIT_AMOUNT);

        double accountBalanceBefore = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountId);

        MakeDepositResponseModel responseModel = UserSteps.makeDeposit(createdUser, makeDeposit);

        double accountBalanceAfter = AccountBalanceUtils.getBalanceForAccount(createdUser.getUsername(),
                createdUser.getPassword(), accountId);

        softly.assertThat(makeDeposit.getBalance()).isEqualTo(responseModel.getBalance());
        softly.assertThat(makeDeposit.getId()).isEqualTo(responseModel.getId());
        softly.assertThat(accountBalanceBefore).isEqualTo(accountBalanceAfter - DEPOSIT_AMOUNT);
    }


}
