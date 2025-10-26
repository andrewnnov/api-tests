package ui.pages;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Getter
public class UserDashBoardPage extends BasePage<UserDashBoardPage> {

    private SelenideElement welcomeText = $(By.className("welcome-text"));
    private SelenideElement createNewAccountButton = $(Selectors.byText("➕ Create New Account"));
    private SelenideElement depositButton = $(byText("\uD83D\uDCB0 Deposit Money"));
    private SelenideElement transferButton =  $(byText("\uD83D\uDD04 Make a Transfer"));

    @Override
    public String url() {
        return "/dashboard";
    }

    public UserDashBoardPage createNewAccount() {
        createNewAccountButton.click();
        return this;
    }

    public UserDashBoardPage makeDeposit() {
        depositButton.click();
        return this;
    }

    public UserDashBoardPage makeTransfer() {
        transferButton.click();
        return this;
    }


}
