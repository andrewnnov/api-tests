package iteration1.ui;

import api.models.CreateUserRequestModel;
import api.models.CreateUserResponseModel;
import api.models.comparison.ModelAssertions;
import api.requests.steps.AdminSteps;
import api.requests.steps.CreateModelSteps;
import common.annotations.AdminSession;
import org.junit.jupiter.api.Test;
import ui.elements.UserBage;
import ui.pages.AdminPanelPage;
import ui.pages.BankAlert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CreateUserTest extends BaseUITest {

    @Test
    @AdminSession
    public void adminCanCreateUserTest() {

        CreateUserRequestModel createUserRequestModel = CreateModelSteps.createUserModel();

        UserBage newUserBage =  new AdminPanelPage().open().createUser(createUserRequestModel.getUsername(), createUserRequestModel.getPassword())
                .checkAlertMessageAndAccept(BankAlert.USER_CREATED_SUCCESSFULLY.getMessage())
                .findUserByUsername(createUserRequestModel.getUsername());

        assertThat(newUserBage).as("UserBage should exist on Dashboard after user creation").isNotNull();


        CreateUserResponseModel createUser = AdminSteps.getAllUsers().stream().filter(user -> user.getUsername()
                .equals(createUserRequestModel.getUsername())).findFirst().get();

        ModelAssertions.assertThatModels(createUserRequestModel, createUser);
    }

    @Test
    @AdminSession
    public void adminCannotCreateUserWithInvalidDataTest() {

        CreateUserRequestModel createUserRequestModel = CreateModelSteps.createUserModel();
        createUserRequestModel.setUsername("a");

        assertTrue(new AdminPanelPage().open()
                .createUser(createUserRequestModel.getUsername(), createUserRequestModel.getPassword())
                .checkAlertMessageAndAccept(BankAlert.USERNAME_MUST_BE_BETWEEN_3_AND_15_CHARACTERS.getMessage())
                .getAllUsers().stream().noneMatch(userBage -> userBage.getUsername().equals(createUserRequestModel.getUsername())));

        long usersWithSameUsername = AdminSteps.getAllUsers().stream().filter(user -> user.getUsername()
                .equals(createUserRequestModel.getUsername())).count();

        assertThat(usersWithSameUsername).isZero();
    }

}
