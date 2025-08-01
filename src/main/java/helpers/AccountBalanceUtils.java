package helpers;

import models.MakeDepositResponseModel;
import requests.GetAccountRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

import java.util.Arrays;

public class AccountBalanceUtils {

    public static double getBalanceForAccount(String username, String password, long accountId) {
        MakeDepositResponseModel[] accounts = new GetAccountRequester(
                RequestSpecs.authAsUser(username, password),
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
