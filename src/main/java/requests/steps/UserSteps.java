package requests.steps;

import io.restassured.response.ValidatableResponse;
import models.*;
import requests.skelethon.Endpoint;
import requests.skelethon.requesters.CrudRequester;
import requests.skelethon.requesters.ValidatedCrudRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class UserSteps {


    public static ValidatableResponse createAccount(CreateUserRequestModel createUserRequestModel) {
        return new CrudRequester(
                RequestSpecs.authAsUser(createUserRequestModel.getUsername(), createUserRequestModel.getPassword()),
                Endpoint.ACCOUNTS,
                ResponseSpecs.entityWasCreated())
                .post(null);
    }

    public static MakeDepositResponseModel makeDeposit(CreateUserRequestModel createdUserModel,
                                                       MakeDepositRequestModel makeDepositRequestModel) {
        return new CrudRequester(RequestSpecs
                .depositAsAuthUser(createdUserModel.getUsername(), createdUserModel.getPassword()),
                Endpoint.DEPOSIT,
                ResponseSpecs.requestReturnsOK())
                .post(makeDepositRequestModel).extract().as(MakeDepositResponseModel.class);
    }

    public static MakeTransferResponseModel makeTransfer(CreateUserRequestModel createdUserModel,
                                                         MakeTransferRequestModel transferRequestModel) {
        return new ValidatedCrudRequester<MakeTransferResponseModel>(
                RequestSpecs.authAsUser(createdUserModel.getUsername(), createdUserModel.getPassword()),
                Endpoint.TRANSFER,
                ResponseSpecs.requestReturnsOK())
                .post(transferRequestModel);
    }



}
