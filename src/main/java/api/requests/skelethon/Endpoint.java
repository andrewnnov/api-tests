package api.requests.skelethon;

import api.models.*;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum Endpoint {

    ADMIN_USER(
            "/admin/users",
            CreateUserRequestModel.class,
            CreateUserResponseModel.class
    ),

    ACCOUNTS (
            "/accounts",
            BaseModel.class,
            CreateAccountResponseModel.class
    ),

    LOGIN (
            "/auth/login",
            LoginUserRequestModel.class,
            LoginUserResponseModel.class
    ),

    DEPOSIT (
            "/accounts/deposit",
            MakeDepositRequestModel.class,
            MakeDepositResponseModel.class
    ),

    TRANSFER (
            "/accounts/transfer",
            MakeTransferRequestModel.class,
            MakeTransferResponseModel.class

    ),

    CHANGE_NAME (
            "/customer/profile",
            ChangeNameRequestModel.class,
            ChangeNameResponseModel.class
    ),

    GET_USER (
            "/customer/profile",
            BaseModel.class,
            GetUserResponseModel.class
    ),

    GET_ACCOUNT (
            "/customer/accounts",
            BaseModel.class,
            MakeDepositResponseModel.class
    ),

    CUSTOMER_ACCOUNT(
            "/customer/accounts",
            BaseModel.class,
            CreateAccountResponseModel.class
    );




    private final String url;
    private final Class<? extends BaseModel> requestModel;
    private final Class<? extends BaseModel> responseModel;

}
