package iteration2.api.negative;

import iteration1.api.BaseTest;
import models.ChangeNameRequestModel;
import models.ChangeNameResponseModel;
import models.CreateUserRequestModel;
import org.junit.jupiter.api.Test;
import requests.skelethon.Endpoint;
import requests.skelethon.requesters.ValidatedCrudRequester;
import requests.steps.AdminSteps;
import requests.steps.CreateModelSteps;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class ChangeNameNegativeTest extends BaseTest {
    //bug name can not be a blanc
    @Test
    public void authUserCanNotUpdateOwnNameWithBlancValue() {
        String newUserName = "   ";
        CreateUserRequestModel createdUser = CreateModelSteps.createUserModel();
        AdminSteps.createUser(createdUser);

        ChangeNameRequestModel changeName = CreateModelSteps.changeNameModel(newUserName);

        ChangeNameResponseModel responseModel = new ValidatedCrudRequester<ChangeNameResponseModel>(
                RequestSpecs.authAsUser(createdUser.getUsername(), createdUser.getPassword()),
                Endpoint.CHANGE_NAME,
                ResponseSpecs.requestReturnOK("Profile updated successfully"))
                .update(changeName);

        softly.assertThat(newUserName).isEqualTo(responseModel.getCustomer().getName());
    }

    //bug I can inject js
    @Test
    public void userCannotInjectJavaScriptInNameField() {
        String newUserName = "<script>alert('XSS')</script>";

        CreateUserRequestModel createdUser = CreateModelSteps.createUserModel();
        AdminSteps.createUser(createdUser);

        ChangeNameRequestModel changeName = CreateModelSteps.changeNameModel(newUserName);

        ChangeNameResponseModel responseModel = new ValidatedCrudRequester<ChangeNameResponseModel>(
                RequestSpecs.authAsUser(createdUser.getUsername(), createdUser.getPassword()),
                Endpoint.CHANGE_NAME,
                ResponseSpecs.requestReturnOK("Profile updated successfully"))
                .update(changeName);

        softly.assertThat(newUserName).isEqualTo(responseModel.getCustomer().getName());
    }
}
