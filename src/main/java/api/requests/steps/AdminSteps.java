package api.requests.steps;

import api.generators.RandomModelGenerator;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requesters.ValidatedCrudRequester;
import api.models.CreateUserRequestModel;
import api.models.CreateUserResponseModel;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;

import java.util.List;

public class AdminSteps {

    public static CreateUserResponseModel createUser(CreateUserRequestModel createUserRequestModel) {
        return new ValidatedCrudRequester<CreateUserResponseModel>(
                RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.entityWasCreated())
                .post(createUserRequestModel);
    }

    //TO DO разобрать этот метод
    public static CreateUserRequestModel createUser() {
        CreateUserRequestModel userRequestModel = RandomModelGenerator.generate(CreateUserRequestModel.class);
        new ValidatedCrudRequester<CreateUserResponseModel>(
                RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.entityWasCreated())
                .post(userRequestModel);

        return userRequestModel;
    }

    public static List<CreateUserResponseModel> getAllUsers() {
        return new ValidatedCrudRequester<CreateUserResponseModel>(
                RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.requestReturnsOK())
                .getAll(CreateUserResponseModel[].class);
    }


}
