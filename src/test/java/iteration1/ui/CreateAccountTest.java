package iteration1.ui;

import api.models.CreateAccountResponseModel;
import api.models.CreateUserRequestModel;
import api.models.CreateUserResponseModel;
import api.requests.steps.AdminSteps;
import api.requests.steps.CreateModelSteps;
import api.requests.steps.UserSteps;
import org.junit.jupiter.api.Test;
import ui.pages.BankAlert;
import ui.pages.UserDashBoard;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class CreateAccountTest extends BaseUiTest {


    @Test
    public void userCanCreateAccountTest() {

        CreateUserRequestModel userModel = CreateModelSteps.createUserModel();
        CreateUserResponseModel user = AdminSteps.createUser(userModel);

        authAsUser(userModel);

        new UserDashBoard().open().createNewAccount();

       List<CreateAccountResponseModel> createdAccounts  = new UserSteps(userModel.getUsername(), userModel.getPassword())
                .getAllAccounts();

       assertThat(createdAccounts).hasSize(1);

        new UserDashBoard()
                .checkAlertMessageAndAccept(BankAlert.NEW_ACCOUNT_CREATED.getMessage()
                        + createdAccounts.getFirst().getAccountNumber());

        assertThat(createdAccounts.getFirst().getBalance()).isZero();
    }

}
