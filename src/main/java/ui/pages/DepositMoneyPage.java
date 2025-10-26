package ui.pages;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class DepositMoneyPage extends BasePage<DepositMoneyPage> {
    private SelenideElement chooseAnAccountField = $(Selectors.byText("-- Choose an account --"));
    private SelenideElement chooseAnAccountNumber = $("select.account-selector");
    private SelenideElement enterAmountInput = $(Selectors.byAttribute("placeholder", "Enter amount"));
    private SelenideElement depositButton =  $(By.xpath("//button[contains(text(),'Deposit')]"));

    @Override
    public String url() {
        return "/deposit";
    }

    public DepositMoneyPage makeDeposit(long accountId, double amount) {
        chooseAnAccountField.click();
        chooseAnAccountNumber.selectOptionContainingText(String.valueOf(accountId));
        enterAmountInput.sendKeys(String.valueOf(amount));
        depositButton.click();
        return this;
    }
}
