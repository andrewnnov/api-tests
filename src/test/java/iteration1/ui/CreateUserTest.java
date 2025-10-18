package iteration1.ui;

import api.models.CreateUserRequestModel;
import api.models.CreateUserResponseModel;
import api.models.comparison.ModelAssertions;
import api.requests.steps.AdminSteps;
import api.requests.steps.CreateModelSteps;
import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import ui.pages.AdminPanelPage;
import ui.pages.BankAlert;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateUserTest extends BaseUITest {

    @Test
    public void adminCanCreateUserTest() {

        CreateUserRequestModel admin = CreateUserRequestModel.getAdmin();

        authAsUser(admin);

        CreateUserRequestModel createUserRequestModel = CreateModelSteps.createUserModel();

        new AdminPanelPage().open().createUser(createUserRequestModel.getUsername(), createUserRequestModel.getPassword())
                .checkAlertMessageAndAccept(BankAlert.USER_CREATED_SUCCESSFULLY.getMessage())
                .getAllUsers().findBy(Condition.exactText(createUserRequestModel.getUsername() + "\nUSER"))
                .shouldBe(Condition.visible);

        CreateUserResponseModel createUser = AdminSteps.getAllUsers().stream().filter(user -> user.getUsername()
                .equals(createUserRequestModel.getUsername())).findFirst().get();

        ModelAssertions.assertThatModels(createUserRequestModel, createUser);
    }

    @Test
    public void adminCannotCreateUserWithInvalidDataTest() {

        CreateUserRequestModel admin = CreateUserRequestModel.getAdmin();

        authAsUser(admin);

        CreateUserRequestModel createUserRequestModel = CreateModelSteps.createUserModel();
        createUserRequestModel.setUsername("a");

        new AdminPanelPage().open().createUser(createUserRequestModel.getUsername(), createUserRequestModel.getPassword())
                .checkAlertMessageAndAccept(BankAlert.USERNAME_MUST_BE_BETWEEN_3_AND_15_CHARACTERS.getMessage())
                .getAllUsers().findBy(Condition.exactText(createUserRequestModel.getUsername() + "\nUSER"))
                .shouldNotBe(Condition.exist);

        long usersWithSameUsername = AdminSteps.getAllUsers().stream().filter(user -> user.getUsername()
                .equals(createUserRequestModel.getUsername())).count();

        assertThat(usersWithSameUsername).isZero();

    }

}
