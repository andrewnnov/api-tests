package iteration2.tests.positive;

import generators.RandomData;
import iteration1.BaseTest;
import models.*;
import org.junit.jupiter.api.Test;
import requests.AdminCreateUserRequester;
import requests.ChangeNameRequester;
import requests.GetUserRequester;
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

        new AdminCreateUserRequester(RequestSpecs.adminSpec(), ResponseSpecs.entityWasCreated())
                .post(createdUser);

        ChangeNameRequestModel changeName = ChangeNameRequestModel.builder()
                .name(newUserName).build();

        ChangeNameResponseModel responseModel = new ChangeNameRequester(RequestSpecs.authAsUser(createdUser.getUsername(),
                createdUser.getPassword()), ResponseSpecs.requestReturnOK("Profile updated successfully"))
                .put(changeName).extract().as(ChangeNameResponseModel.class);

        GetNameResponseModel responseModelAfter = new GetUserRequester(RequestSpecs.authAsUser(createdUser.getUsername(),
                createdUser.getPassword()), ResponseSpecs.requestReturnsOK()).get().extract().as(GetNameResponseModel.class);

        softly.assertThat(newUserName).isEqualTo(responseModelAfter.getName());
    }
}
