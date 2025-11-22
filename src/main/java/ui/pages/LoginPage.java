package ui.pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import javax.swing.plaf.TableHeaderUI;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage> {
    private SelenideElement loginButton = $(By.xpath("//button[text()='Login']"));

    @Override
    public String url() {
        return "/login";
    }

    public LoginPage login(String username, String password) {
        usernameInput.shouldBe(visible)
                .setValue(username)
                .shouldHave(value(username));

        passwordInput.shouldBe(visible)
                .setValue(password)
                .shouldHave(value(password));

        loginButton.shouldBe(visible, enabled)
                .click();

        return this;
    }
}
