package iteration1;

import generators.RandomData;
import generators.RandomModelGenerator;
import models.CreateUserRequestModel;
import models.UserRole;
import org.junit.jupiter.api.Test;
import requests.skelethon.Endpoint;
import requests.skelethon.requesters.CrudRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class CreateAccountTest extends BaseTest {

    @Test
    public void userCanCreateAccountTest() {
        //create user
        CreateUserRequestModel createUserRequestModel =
                RandomModelGenerator.generate(CreateUserRequestModel.class);

        new CrudRequester(RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.entityWasCreated())
                .post(createUserRequestModel);

        new CrudRequester(RequestSpecs.authAsUser(createUserRequestModel.getUsername(),
                createUserRequestModel.getPassword()),
                Endpoint.ACCOUNTS,
                ResponseSpecs.entityWasCreated())
                .post(null);

        //check all users account and make sure that created account has in that list
    }
}
