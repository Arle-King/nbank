package org.example.ui.pages;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import org.example.api.models.accoints.accounts.CreateAccountResponseDTO;
import org.example.api.models.admin.users.CreateUserRequestDTO;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

@Getter
public class TransferPage extends BasePage<TransferPage>{
    @Override
    public String endpoint() {
        return "/transfer";
    }

    private String xpathSelectAccount = "//select[@class='form-control account-selector']";
    private String xpathRecipientName = "//input[@placeholder='Enter recipient name']";
    private String xpathReicpientAccountNumber = "//input[@placeholder='Enter recipient account number']";
    private String xpathAmount = "//input[@placeholder='Enter amount']";
    private String xpathSendTransferButton = "//button[contains(text(), 'Send Transfer')]";
    private String xpathCheckBoxConfirm = "//input[@id='confirmCheck']";
    private String xpathTransfer = "//*[contains(text(), 'Make a Transfer')]";

    private SelenideElement selectAccount = $(By.xpath(xpathSelectAccount));
    private SelenideElement recipientName = $(By.xpath(xpathRecipientName));
    private SelenideElement reicpientAccountNumber = $(By.xpath(xpathReicpientAccountNumber));
    private SelenideElement amount = $(By.xpath(xpathAmount));
    private SelenideElement checkBoxConfirm = $(By.xpath(xpathCheckBoxConfirm));
    private SelenideElement sendTransferButton = $(By.xpath(xpathSendTransferButton));
    private SelenideElement transferWelcomeText = $(By.xpath(xpathTransfer));

    public TransferPage transfer(CreateAccountResponseDTO userAccount1, CreateUserRequestDTO user, CreateAccountResponseDTO userAccount2, Double negativeTransfer) {
        selectAccount.selectOptionContainingText(userAccount1.getAccountNumber());
        recipientName.sendKeys(user.getUsername());
        reicpientAccountNumber.sendKeys(userAccount2.getAccountNumber());
        amount.sendKeys(negativeTransfer.toString());
        checkBoxConfirm.click();
        sendTransferButton.click();
        return this;
    }
}
