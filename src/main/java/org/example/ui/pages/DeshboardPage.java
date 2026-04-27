package org.example.ui.pages;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

@Getter
public class DeshboardPage extends BasePage<DeshboardPage> {
    String xpathDashboard = "//*[contains(text(), 'User Dashboard')]";
    String xpathWelcomeText = "//*[contains(text(), 'Welcome, ')]";
    String xpathUsername = "//span[@class='user-name']";

    @Override
    public String endpoint() {
        return "/dashboard";
    }

    SelenideElement dashboard = $(By.xpath(xpathDashboard));
    SelenideElement welcomeText = $(By.xpath(xpathWelcomeText));
    SelenideElement username = $(By.xpath(xpathUsername));

}
