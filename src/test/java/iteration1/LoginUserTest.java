package iteration1;

import models.CreateUserRequestModel;
import models.LoginUserRequestModel;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import requests.skelethon.Endpoint;
import requests.skelethon.requesters.CrudRequester;
import requests.skelethon.requesters.ValidatedCrudRequester;
import requests.steps.AdminSteps;
import requests.steps.CreateModelSteps;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class LoginUserTest extends BaseTest {

    @Test
    public void adminCanGenerateAuthTokenTest() {

        LoginUserRequestModel loginUserRequestModel = LoginUserRequestModel.builder()
                .username("admin")
                .password("admin")
                .build();

        new ValidatedCrudRequester<CreateUserRequestModel>(RequestSpecs.unauthSpec(),
                Endpoint.LOGIN,
                ResponseSpecs.requestReturnsOK())
                .post(loginUserRequestModel);
    }

    @Test
    public void userCanGenerateAuthTokenTest() {

        CreateUserRequestModel createUserRequestModel = CreateModelSteps.createUserModel();
        AdminSteps.createUser(createUserRequestModel);

        new CrudRequester(RequestSpecs.unauthSpec(),
                Endpoint.LOGIN,
                ResponseSpecs.requestReturnsOK())
                .post(LoginUserRequestModel.builder()
                        .username(createUserRequestModel.getUsername())
                        .password(createUserRequestModel.getPassword()).build())
                .header("Authorization", Matchers.notNullValue());
    }
}
