package iteration2.tests.negative;

import generators.RandomData;
import generators.RandomModelGenerator;
import iteration1.BaseTest;
import models.ChangeNameRequestModel;
import models.ChangeNameResponseModel;
import models.CreateUserRequestModel;
import models.UserRole;
import org.junit.jupiter.api.Test;
import requests.skelethon.Endpoint;
import requests.skelethon.requesters.CrudRequester;
import requests.skelethon.requesters.ValidatedCrudRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class ChangeNameNegativeTest extends BaseTest {
    //bug name can not be a blanc
    @Test
    public void authUserCanNotUpdateOwnNameWithBlancValue() {
        String newUserName = "   ";
        CreateUserRequestModel createdUser = RandomModelGenerator.generate(CreateUserRequestModel.class);

        new CrudRequester(RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.entityWasCreated())
                .post(createdUser);

        ChangeNameRequestModel changeName = ChangeNameRequestModel.builder()
                .name(newUserName).build();

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

        CreateUserRequestModel createdUser = RandomModelGenerator.generate(CreateUserRequestModel.class);

        new CrudRequester(RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.entityWasCreated())
                .post(createdUser);

        ChangeNameRequestModel changeName = ChangeNameRequestModel.builder()
                .name(newUserName).build();

        ChangeNameResponseModel responseModel = new ValidatedCrudRequester<ChangeNameResponseModel>(
                RequestSpecs.authAsUser(createdUser.getUsername(), createdUser.getPassword()),
                Endpoint.CHANGE_NAME,
                ResponseSpecs.requestReturnOK("Profile updated successfully"))
                .update(changeName);

        softly.assertThat(newUserName).isEqualTo(responseModel.getCustomer().getName());
    }
}
