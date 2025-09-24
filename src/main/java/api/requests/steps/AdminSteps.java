package api.requests.steps;

import api.models.CreateUserRequestModel;
import api.models.CreateUserResponseModel;
import api.requests.skelethon.requesters.ValidatedCrudRequester;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;
import api.requests.skelethon.Endpoint;

public class AdminSteps {

    public static CreateUserResponseModel createUser(CreateUserRequestModel createUserRequestModel) {
        return new ValidatedCrudRequester<CreateUserResponseModel>(
                RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.entityWasCreated())
                .post(createUserRequestModel);
    }
}
