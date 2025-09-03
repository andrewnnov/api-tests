package iteration1.api;

import models.CreateUserRequestModel;
import models.CreateUserResponseModel;
import models.comparison.ModelAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import requests.skelethon.Endpoint;
import requests.skelethon.requesters.CrudRequester;
import requests.steps.AdminSteps;
import requests.steps.CreateModelSteps;
import specs.RequestSpecs;
import specs.ResponseSpecs;

import java.util.stream.Stream;

public class CreateUserTest extends BaseTest {


    @Test
    public void adminCanCreateUserWithCorrectData() {

        CreateUserRequestModel createUserRequestModel = CreateModelSteps.createUserModel();
        CreateUserResponseModel createUserResponseModel = AdminSteps.createUser(createUserRequestModel);

        ModelAssertions.assertThatModels(createUserRequestModel, createUserResponseModel).match();
    }

    public static Stream<Arguments> userInvalidData() {
        return Stream.of(
                //username field validation
                Arguments.of(
                        "      ",
                        "Password4!!",
                        "USER",
                        "username",
                        "Username cannot be blank"),
                Arguments.of(
                        "ab",
                        "Password4!!",
                        "USER",
                        "username",
                        "Username must be between 3 and 15 characters"),
                Arguments.of(
                        "abc#",
                        "Password4!!",
                        "USER",
                        "username",
                        "Username must contain only letters, digits, dashes, underscores, and dots"),
                Arguments.of(
                        "abc%",
                        "Password4!!",
                        "USER",
                        "username",
                        "Username must contain only letters, digits, dashes, underscores, and dots")

        );
    }

    @MethodSource("userInvalidData")
    @ParameterizedTest
    public void adminCanNotCreateUserWithInvalidData(String username, String password, String role, String errorKey, String errorValue) {

        CreateUserRequestModel createUserRequestModel = CreateUserRequestModel.builder()
                .username(username)
                .password(password)
                .role(role).build();

        new CrudRequester(RequestSpecs.adminSpec(),
                Endpoint.ADMIN_USER,
                ResponseSpecs.requestReturnsBadRequest(errorKey, errorValue))
                .post(createUserRequestModel);
    }
}
