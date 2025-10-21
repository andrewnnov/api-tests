package iteration1.ui;

import api.models.CreateAccountResponseModel;
import common.annotations.UserSession;
import common.storage.SessionStorage;
import org.junit.jupiter.api.Test;
import ui.pages.BankAlert;
import ui.pages.UserDashBoardPage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class CreateAccountTest extends BaseUITest {

    @Test
    @UserSession()
    public void userCanCreateAccountTest() {

        new UserDashBoardPage().open().createNewAccount();

        List<CreateAccountResponseModel> createdAccounts = SessionStorage.getSteps().getAllAccounts();

        assertThat(createdAccounts).hasSize(1);

        new UserDashBoardPage().checkAlertMessageAndAccept(BankAlert.NEW_ACCOUNT_CREATED.getMessage() + createdAccounts.getFirst().getAccountNumber());

        assertThat(createdAccounts.getFirst().getBalance()).isZero();
    }

}
