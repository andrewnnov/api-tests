package ui.pages;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

@Getter
public class UserDashBoardPage extends BasePage<UserDashBoardPage> {
    private SelenideElement welcomeText = $(Selectors.byClassName("welcome-text"));
    private SelenideElement createNewAccountButton =  $(Selectors.byText("➕ Create New Account"));
    private SelenideElement depositMoneyButton = $(By.xpath("//button[contains(text(),'Deposit')]"));

    @Override
    public String url() {
        return "/dashboard";
    }

    public UserDashBoardPage createNewAccount() {
        createNewAccountButton.click();
        return this;
    }

    public UserDashBoardPage depositMoney() {
        depositMoneyButton.click();
        return this;
    }
}
