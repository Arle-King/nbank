package org.example.ui.pages;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

@Getter
public class CustomerNamePage extends BasePage<CustomerNamePage>{

    String xpathFieldEditNewName = "//input[@placeholder = 'Enter new name']";
    String xpathSaveButton = "//button[contains(text(), 'Save Changes')]";
    String xpathEditProfile = "//*[contains(text(), 'Edit Profile')]";

    SelenideElement fieldEditNewName = $(By.xpath(xpathFieldEditNewName));
    SelenideElement selenideElement = $(By.xpath(xpathSaveButton));
    SelenideElement editProfile = $(By.xpath(xpathEditProfile));

    @Override
    public String endpoint() {
        return "/edit-profile";
    }

    public CustomerNamePage editName(String newName) {
        fieldEditNewName.sendKeys(newName);
        selenideElement.click();
        return this;
    }
}
