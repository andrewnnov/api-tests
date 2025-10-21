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

    public static CreateUserRequestModel createUser() {

        CreateUserRequestModel createUserRequestModel
                = RandomModelGenerator.generate(CreateUserRequestModel.class);

        new ValidatedCrudRequester<CreateUserResponseModel>(
                RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.entityWasCreated())
                .post(createUserRequestModel);

        return createUserRequestModel;
    }



    public static List<CreateUserResponseModel> getAllUsers() {
        return new ValidatedCrudRequester<CreateUserResponseModel>(
                RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.requestReturnsOK())
                .getAll(CreateUserResponseModel[].class);
    }


}
