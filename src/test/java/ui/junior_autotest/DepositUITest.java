package ui.junior_autotest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.example.BankWidget;
import org.example.api.generators.RandomModelGenerator;
import org.example.api.models.accoints.accounts.CreateAccountResponseDTO;
import org.example.api.models.admin.users.CreateUserRequestDTO;
import org.example.api.skelethon.enams.Endpoint;
import org.example.api.skelethon.requests.CrudRequest;
import org.example.api.specs.RequestSpecs;
import org.example.api.specs.ResponceSpecs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;

import java.util.Map;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.*;
import static org.example.BankWidget.deleteUser;
import static org.example.BankWidget.getAccountById;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DepositUITest {

    CreateUserRequestDTO user;

    CreateAccountResponseDTO userAccount;

    Double banalce = Math.random() * 1000 / 10;

    String xpathSelectAccount = "//select[@class='form-control account-selector']";
    String xpathEntelAmaunt = "//input[@placeholder='Enter amount']";
    String xpathDepositButton = "//button[contains(text(), 'Deposit')]";
    String xpathDashboard = "//*[contains(text(), 'User Dashboard')]";
    String xpathDepositMoney = "//*[contains(text(), 'Deposit Money')]";

    static final String baseUrl = "http://192.168.0.137:3000";
    static final String loginEndpoint = "/login";
    static final String depositEndpoint = "/deposit";
    static final String deshboardEndpoint = "/dashboard";


    Alert alert;

    @BeforeAll
    public static void setupSelenoid() {
        Configuration.remote = "http://localhost:4444/wd/hub";
        Configuration.baseUrl = baseUrl;
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";

        Configuration.browserCapabilities.setCapability("selenoid:options",
                Map.of("enableVNC", true, "enableLog", true)
        );
    }

    @BeforeEach
    public void precondition() {

        user = RandomModelGenerator.generate(CreateUserRequestDTO.class);

        String token = new CrudRequest(RequestSpecs.getAdminSpec(),
                Endpoint.CREATE_USER,
                ResponceSpecs.entityWasCreated())
                .post(user)
                .extract()
                .header(ResponceSpecs.AUTH_HEADER);

        userAccount = BankWidget.createAccount(user.getUsername(), user.getPassword());

        Selenide.open(loginEndpoint);

        executeJavaScript("localStorage.setItem('authToken', arguments[0])", token);

        Selenide.open(depositEndpoint);
    }

    @AfterEach
    public void postcondition() {
        deleteUser(user.getUsername());
    }

    @Test
    public void testPositiveDeposit() {
        $(By.xpath(xpathSelectAccount)).selectOptionContainingText(userAccount.getAccountNumber());
        $(By.xpath(xpathEntelAmaunt)).sendKeys(banalce.toString());
        $(By.xpath(xpathDepositButton)).click();

        alert = switchTo().alert();

        assertTrue(alert.getText().contains("Successfully deposited $" + banalce.toString() + " to account " + userAccount.getAccountNumber() + "!"));

        alert.accept();

        assertEquals(baseUrl + deshboardEndpoint, WebDriverRunner.url());
        $(By.xpath(xpathDashboard)).shouldBe(Condition.visible);

        assertEquals(getAccountById(userAccount.getId()).getBalance(), banalce);
    }

    @Test
    public void testNegativeDeposit() {
        banalce+= 5000;

        $(By.xpath(xpathSelectAccount)).selectOptionContainingText(userAccount.getAccountNumber());
        $(By.xpath(xpathEntelAmaunt)).sendKeys(banalce.toString());
        $(By.xpath(xpathDepositButton)).click();

        alert = switchTo().alert();

        assertTrue(alert.getText().contains("Please deposit less or equal to 5000$"));

        alert.accept();

        assertEquals(baseUrl + depositEndpoint, WebDriverRunner.url());
        $(By.xpath(xpathDepositMoney)).shouldBe(Condition.visible);
        assertTrue(Objects.requireNonNull($(".account-selector").getSelectedOptionText()).contains(userAccount.getAccountNumber()));

        assertEquals(getAccountById(userAccount.getId()).getBalance(), userAccount.getBalance());
    }
}
