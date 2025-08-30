package requests.steps;

import generators.RandomModelGenerator;
import models.BaseModel;
import models.CreateUserRequestModel;
import models.CreateUserResponseModel;
import requests.skelethon.Endpoint;
import requests.skelethon.requesters.ValidatedCrudRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class AdminSteps {

    public static CreateUserResponseModel createUser(CreateUserRequestModel createUserRequestModel) {
        return new ValidatedCrudRequester<CreateUserResponseModel>(
                RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.entityWasCreated())
                .post(createUserRequestModel);
    }
}
