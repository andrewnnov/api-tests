package requests.skelethon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import models.*;
import requests.LoginUserRequester;

@Getter
@AllArgsConstructor
public enum Endpoint {

    ADMIN_USER(
           "/admin/users",
            CreateUserRequestModel.class,
            CreateUserResponseModel.class
    ),

    ACCOUNTS(
            "/accounts",
            CreateUserRequestModel.class,
            CreateAccountResponseModel.class
    ),

    LOGIN (
          "/auth/login",
            LoginUserRequestModel.class,
            LoginUserResponseModel.class
    );

    private String url;
    private final Class<? extends BaseModel> requestModel;
    private final Class<? extends BaseModel> responseModel;
}
