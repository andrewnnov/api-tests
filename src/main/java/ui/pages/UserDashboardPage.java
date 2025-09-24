package ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;
@Getter
public class UserDashboardPage extends BasePage<UserDashboardPage> {
    private SelenideElement welcomeText = $(Selectors.byClassName("welcome-text"));

    @Override
    public String url() {
        return "/dashboard";
    }
}
