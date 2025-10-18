package ui.pages;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class MakeTransferPage extends BasePage<MakeTransferPage> {

    private SelenideElement chooseAnAccountField = $(Selectors.byText("-- Choose an account --"));
    private SelenideElement chooseAnAccountNumber = $("select.account-selector");
    private SelenideElement chooseRecipientAccountNumber = $(Selectors.byAttribute("placeholder", "Enter recipient account number"));
    private SelenideElement enterTransferAmountInput = $(Selectors.byAttribute("placeholder", "Enter amount"));
    private SelenideElement confirmCheckButton = $(Selectors.byId("confirmCheck"));
    private SelenideElement sendTransferButton = $(By.xpath("//button[contains(text(),'Send Transfer')]"));

    @Override
    public String url() {
        return "/transfer";
    }

    public MakeTransferPage makeTransfer(long fromAccountId, long toAccountId, double amount) {
        chooseAnAccountField.click();
        chooseAnAccountNumber.selectOptionContainingText(String.valueOf(fromAccountId));
        chooseRecipientAccountNumber.sendKeys("ACC" + toAccountId);
        enterTransferAmountInput.sendKeys(String.valueOf(amount));
        confirmCheckButton.click();
        sendTransferButton.click();
        return this;
    }
}
