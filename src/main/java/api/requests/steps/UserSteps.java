package api.requests.steps;

import api.helpers.AccountBalanceUtils;
import api.models.*;
import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requesters.CrudRequester;
import api.requests.skelethon.requesters.ValidatedCrudRequester;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;
import io.restassured.response.ValidatableResponse;

import java.util.List;

public class UserSteps {

    private String username;
    private String password;

    public UserSteps(String username, String password) {
        this.username = username;
        this.password = password;
    }

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

    public static long getAccountID(ValidatableResponse validatableResponse) {
        return ((Integer) validatableResponse.extract().path("id")).longValue();
    }

    public static ChangeNameResponseModel changeName(CreateUserRequestModel createdUser, ChangeNameRequestModel newUserName) {
        return new ValidatedCrudRequester<ChangeNameResponseModel>(
                RequestSpecs.authAsUser(createdUser.getUsername(),
                        createdUser.getPassword()),
                Endpoint.CHANGE_NAME,
                ResponseSpecs.requestReturnOK("Profile updated successfully"))
                .update(newUserName);
    }

    public static GetUserResponseModel getUser(CreateUserRequestModel createdUser) {
        return (GetUserResponseModel) new ValidatedCrudRequester<GetUserResponseModel>(
                RequestSpecs.authAsUser(createdUser.getUsername(), createdUser.getPassword()),
                Endpoint.GET_USER,
                ResponseSpecs.requestReturnsOK()).get();
    }

    public List<CreateAccountResponseModel> getAllAccounts() {
        return new ValidatedCrudRequester<CreateAccountResponseModel>(
                RequestSpecs.authAsUser(username, password),
                Endpoint.CUSTOMER_ACCOUNTS,
                ResponseSpecs.requestReturnsOK())
                .getAll(CreateAccountResponseModel[].class);
    }










}
