package iteration2.api.negative;

import iteration1.api.BaseTest;
import api.models.ChangeNameRequestModel;
import api.models.ChangeNameResponseModel;
import api.models.CreateUserRequestModel;
import org.junit.jupiter.api.Test;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requesters.ValidatedCrudRequester;
import api.requests.steps.AdminSteps;
import api.requests.steps.CreateModelSteps;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;

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
