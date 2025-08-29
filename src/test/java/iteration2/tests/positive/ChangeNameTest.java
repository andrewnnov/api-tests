package iteration2.tests.positive;

import generators.RandomData;
import iteration1.BaseTest;
import models.*;
import org.junit.jupiter.api.Test;
import requests.skelethon.Endpoint;
import requests.skelethon.requesters.CrudRequester;
import requests.skelethon.requesters.ValidatedCrudRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class ChangeNameTest extends BaseTest {
    private String newUserName = "Anna";

    @Test
    public void authUserCanUpdateOwnName() {

        CreateUserRequestModel createdUser = CreateUserRequestModel.builder()
                .username(RandomData.getUserName())
                .password(RandomData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        new CrudRequester(RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.entityWasCreated())
                .post(createdUser);

        ChangeNameRequestModel changeName = ChangeNameRequestModel.builder()
                .name(newUserName).build();

        ChangeNameResponseModel responseModel = new ValidatedCrudRequester<ChangeNameResponseModel>(
                RequestSpecs.authAsUser(createdUser.getUsername(),
                createdUser.getPassword()),
                Endpoint.CHANGE_NAME,
                ResponseSpecs.requestReturnOK("Profile updated successfully"))
                .update(changeName);

        GetUserResponseModel responseModelAfter = (GetUserResponseModel) new ValidatedCrudRequester<GetUserResponseModel>(
                RequestSpecs.authAsUser(createdUser.getUsername(), createdUser.getPassword()),
                Endpoint.GET_USER,
                ResponseSpecs.requestReturnsOK()).get();

        softly.assertThat(newUserName).isEqualTo(responseModel.getCustomer().getName());
        softly.assertThat(newUserName).isEqualTo(responseModelAfter.getName());
    }
}
