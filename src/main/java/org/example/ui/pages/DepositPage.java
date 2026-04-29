package org.example.ui.pages;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

@Getter
public class DepositPage extends BasePage<DepositPage> {

    String xpathSelectAccount = "//select[@class='form-control account-selector']";
    String xpathEntelAmaunt = "//input[@placeholder='Enter amount']";
    String xpathDepositButton = "//button[contains(text(), 'Deposit')]";
    String xpathDepositHeading = "//*[contains(text(), 'Deposit Money')]";


    private SelenideElement selectAccount = $(By.xpath(xpathSelectAccount));
    private SelenideElement enterAmount = $(By.xpath(xpathEntelAmaunt));
    private SelenideElement sendButton = $(By.xpath(xpathDepositButton));
    private SelenideElement depositHeading = $(By.xpath(xpathDepositHeading));

    @Override
    public String endpoint() {
        return "/deposit";
    }

    public DepositPage deposit(String accountBumber, Double amount) {
        selectAccount.selectOptionContainingText(accountBumber);
        enterAmount.sendKeys(amount.toString());
        sendButton.click();
        return this;
    }


}
