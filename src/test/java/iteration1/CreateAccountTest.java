package iteration1;

import generators.RandomData;
import generators.RandomModelGenerator;
import models.CreateUserRequestModel;
import models.UserRole;
import org.junit.jupiter.api.Test;
import requests.AdminCreateUserRequester;
import requests.CreateAccountRequester;
import requests.skelethon.Endpoint;
import requests.skelethon.requesters.CrudRequester;
import requests.skelethon.requesters.ValidatedCrudRequester;
import requests.steps.AdminSteps;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class CreateAccountTest extends BaseTest {

    @Test
    public void userCanCreateAccountTest() {
        //create user
        CreateUserRequestModel createUserRequestModel = AdminSteps.createUser();

        new CrudRequester(RequestSpecs.authAsUser(createUserRequestModel.getUsername(),
                createUserRequestModel.getPassword()),
                Endpoint.ACCOUNTS,
                ResponseSpecs.entityWasCreated())
                .post(null);

        new CrudRequester(RequestSpecs.authAsUser(createUserRequestModel.getUsername(),
                createUserRequestModel.getPassword()),
                Endpoint.ACCOUNTS,
                ResponseSpecs.entityWasCreated())
                .post(null);

        //check all users account and make sure that created account has in that list
    }
}
