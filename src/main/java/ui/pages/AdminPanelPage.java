package ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;

@Getter
public class AdminPanelPage extends BasePage<AdminPanelPage> {
    private SelenideElement adminPanelText = $(Selectors.byText("Admin Panel"));
    private SelenideElement addUserButton =  $(Selectors.byText("Add User"));

    @Override
    public String url() {
        return "/admin";
    }

    public AdminPanelPage createUser(String username, String password) {
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        addUserButton.click();
        return this;
    }

    public ElementsCollection getAllUsers() {
        return $(Selectors.byText("All Users"))
                .parent().findAll("li");
    }
}
