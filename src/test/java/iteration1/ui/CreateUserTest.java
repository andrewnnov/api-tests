package iteration1.ui;

import com.codeborne.selenide.*;
import models.CreateUserRequestModel;
import models.CreateUserResponseModel;
import models.comparison.ModelAssertions;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import requests.steps.AdminSteps;
import requests.steps.CreateModelSteps;
import specs.RequestSpecs;

import java.util.Arrays;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.switchTo;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateUserTest {

    @BeforeAll
    public static void setupSelenoid() {
        Configuration.remote = "http://localhost:4444/wd/hub";
        Configuration.baseUrl = "http://192.168.100.53:3000";
        Configuration.browserSize = "1920x1080";
        Configuration.browser="chrome";
        Configuration.browserVersion = "91.0";

        Configuration.browserCapabilities.setCapability("selenoid:options",
                Map.of("enableVNC", true,
                        "enableLog", true));
    }

    @Test
    public void adminCanCreateUserTest() {
        //step 1  admin login in bank
        CreateUserRequestModel admin = CreateUserRequestModel.builder()
                .username("admin")
                .password("admin").build();

        Selenide.open("/login");
        $(Selectors.byAttribute("placeholder", "Username")).sendKeys(admin.getUsername());
        $(Selectors.byAttribute("placeholder", "Password")).sendKeys(admin.getPassword());
        $("button").click();
        $(Selectors.byText("Admin Panel")).shouldBe(Condition.visible);

        CreateUserRequestModel createUserRequestModel = CreateModelSteps.createUserModel();


        //step 2: admin create user in bank
        $(Selectors.byAttribute("placeholder", "Username")).sendKeys(createUserRequestModel.getUsername());
        $(Selectors.byAttribute("placeholder", "Password")).sendKeys(createUserRequestModel.getPassword());
        $(Selectors.byText("Add User")).click();

        //step 3 check that alert User created ...
        Alert alert = switchTo().alert();
        assertEquals(alert.getText(), "✅ User created successfully!");
        alert.accept();

        //step 4 check user in UI
        ElementsCollection allUsers = $(Selectors.byText("All Users"))
                .parent().findAll("li");
        allUsers.findBy(Condition.exactText(createUserRequestModel.getUsername() + "\nUSER"))
                .shouldBe(Condition.visible);

        //step 5 check user in API
        CreateUserResponseModel[] users = given()
                .spec(RequestSpecs.adminSpec())
                .get("http://localhost:4111/api/v1/admin/users")
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(CreateUserResponseModel[].class);

        CreateUserResponseModel createUser = Arrays.stream(users).filter(user -> user.getUsername()
                .equals(createUserRequestModel.getUsername())).findFirst().get();

        ModelAssertions.assertThatModels(createUserRequestModel, createUser);
    }

    @Test
    public void adminCannotCreateUserWithInvalidDataTest() {
        //step 1  admin login in bank
        CreateUserRequestModel admin = CreateUserRequestModel.builder()
                .username("admin")
                .password("admin").build();

        Selenide.open("/login");
        $(Selectors.byAttribute("placeholder", "Username")).sendKeys(admin.getUsername());
        $(Selectors.byAttribute("placeholder", "Password")).sendKeys(admin.getPassword());
        $("button").click();
        $(Selectors.byText("Admin Panel")).shouldBe(Condition.visible);

        CreateUserRequestModel createUserRequestModel = CreateModelSteps.createUserModel();
        createUserRequestModel.setUsername("a");


        //step 2: admin create user in bank
        $(Selectors.byAttribute("placeholder", "Username")).sendKeys(createUserRequestModel.getUsername());
        $(Selectors.byAttribute("placeholder", "Password")).sendKeys(createUserRequestModel.getPassword());
        $(Selectors.byText("Add User")).click();

        //step 3 check that alert User created ...
        Alert alert = switchTo().alert();
        assertThat(alert.getText().contains("fdfdf"));
        alert.accept();

        //step 4 check user not in UI
        ElementsCollection allUsers = $(Selectors.byText("All Users"))
                .parent().findAll("li");
        allUsers.findBy(Condition.exactText(createUserRequestModel.getUsername() + "\nUSER"))
                .shouldNotBe(Condition.exist);

        //step 5 check user not in API
        CreateUserResponseModel[] users = given()
                .spec(RequestSpecs.adminSpec())
                .get("http://localhost:4111/api/v1/admin/users")
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(CreateUserResponseModel[].class);

        long usersWithSameUsername = Arrays.stream(users).filter(user -> user.getUsername()
                .equals(createUserRequestModel.getUsername())).count();

        assertThat(usersWithSameUsername).isEqualTo(0);

    }

}
