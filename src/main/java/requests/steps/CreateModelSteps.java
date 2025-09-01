package requests.steps;

import generators.RandomModelGenerator;
import io.restassured.response.ValidatableResponse;
import models.ChangeNameRequestModel;
import models.CreateUserRequestModel;
import models.MakeDepositRequestModel;
import models.MakeTransferRequestModel;
import requests.skelethon.Endpoint;
import requests.skelethon.requesters.CrudRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class CreateModelSteps {

    public static CreateUserRequestModel createUserModel() {
        return RandomModelGenerator.generate(CreateUserRequestModel.class);
    }


    public static MakeDepositRequestModel createDepositModel(long accountId, double balance) {
        return MakeDepositRequestModel.builder()
                .id(accountId)
                .balance(balance).build();
    }

    public static MakeTransferRequestModel createTransferModel(long accountIdOne, long accountIdTwo, double balance) {
        return MakeTransferRequestModel.builder()
                .senderAccountId(accountIdOne)
                .receiverAccountId(accountIdTwo)
                .amount(balance).build();
    }

    public static ChangeNameRequestModel changeNameModel(String newUserName) {
        return ChangeNameRequestModel.builder()
                .name(newUserName).build();
    }


}
