package api.requests.steps;

import api.models.ChangeNameRequestModel;
import api.models.MakeDepositRequestModel;
import api.models.MakeTransferRequestModel;
import api.generators.RandomModelGenerator;
import api.models.CreateUserRequestModel;

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
