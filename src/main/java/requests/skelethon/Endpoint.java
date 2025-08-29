package requests.skelethon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import models.*;

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

    );

    private final String url;
    private final Class<? extends BaseModel> requestModel;
    private final Class<? extends BaseModel> responseModel;

}
