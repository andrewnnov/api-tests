package iteration1.api;

import api.models.CreateUserRequestModel;
import api.models.LoginUserRequestModel;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requesters.CrudRequester;
import api.requests.skelethon.requesters.ValidatedCrudRequester;
import api.requests.steps.AdminSteps;
import api.requests.steps.CreateModelSteps;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;

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
