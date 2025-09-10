package api.requests.steps;

import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requesters.ValidatedCrudRequester;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;
import api.models.CreateUserRequestModel;
import api.models.CreateUserResponseModel;

import java.util.List;

public class AdminSteps {

    public static CreateUserResponseModel createUser(CreateUserRequestModel createUserRequestModel) {
        return new ValidatedCrudRequester<CreateUserResponseModel>(
                RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.entityWasCreated())
                .post(createUserRequestModel);
    }

    public static List<CreateUserResponseModel> getAllUsers() {
        return new ValidatedCrudRequester<CreateUserResponseModel>(
                RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.requestReturnsOK()
        ).getAll(CreateUserResponseModel[].class);
    }
}
