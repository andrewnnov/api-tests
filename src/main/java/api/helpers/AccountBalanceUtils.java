package api.helpers;

import api.requests.skelethon.Endpoint;
import api.requests.skelethon.requesters.CrudRequester;
import api.models.MakeDepositResponseModel;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;

import java.util.Arrays;

public class AccountBalanceUtils {

    public static double getBalanceForAccount(String username, String password, long accountId) {
        MakeDepositResponseModel[] accounts = new CrudRequester(
                RequestSpecs.authAsUser(username, password),
                Endpoint.GET_ACCOUNT,
                ResponseSpecs.requestReturnsOK()
        ).get()
                .extract()
                .as(MakeDepositResponseModel[].class);

        return Arrays.stream(accounts)
                .filter(acc -> acc.getId() == accountId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Account with id " + accountId + " not found"))
                .getBalance();
    }
}
