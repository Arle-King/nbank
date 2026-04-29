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
import static com.codeborne.selenide.Selenide.$;
import static org.example.BankWidget.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransferUITest {

    CreateUserRequestDTO user;

    CreateAccountResponseDTO userAccount1;
    CreateAccountResponseDTO userAccount2;

    static final String baseUrl = "http://192.168.0.137:3000";
    static final String loginEndpoint = "/login";
    static final String transferEndpoint = "/transfer";
    static final String deshboardEndpoint = "/dashboard";

    String xpathSelectAccount = "//select[@class='form-control account-selector']";
    String xpathRecipientName = "//input[@placeholder='Enter recipient name']";
    String xpathReicpientAccountNumber = "//input[@placeholder='Enter recipient account number']";
    String xpathAmount = "//input[@placeholder='Enter amount']";
    String xpathSendTransferButton = "//button[contains(text(), 'Send Transfer')]";
    String xpathDashboard = "//*[contains(text(), 'User Dashboard')]";
    String xpathCheckBoxConfirm = "//input[@id='confirmCheck']";
    String xpathTransfer = "//*[contains(text(), 'Make a Transfer')]";


    Double amount = 4000.0;
    Double positiveTransfer = 3000.0;
    Double negativeTransfer = 5000.0;

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

        userAccount1 = BankWidget.createAccount(user.getUsername(), user.getPassword());
        userAccount2 = BankWidget.createAccount(user.getUsername(), user.getPassword());

        dodepInAccount(user.getUsername(), user.getPassword(), userAccount1.getId(), amount);

        Selenide.open(loginEndpoint);

        executeJavaScript("localStorage.setItem('authToken', arguments[0])", token);

        Selenide.open(transferEndpoint);
    }

    @AfterEach
    public void postcondition() {
        deleteUser(user.getUsername());
    }

    @Test
    public void testPositiveTransfer() {
        $(By.xpath(xpathSelectAccount)).selectOptionContainingText(userAccount1.getAccountNumber());
        $(By.xpath(xpathRecipientName)).sendKeys(user.getUsername());
        $(By.xpath(xpathReicpientAccountNumber)).sendKeys(userAccount2.getAccountNumber());
        $(By.xpath(xpathAmount)).sendKeys(negativeTransfer.toString());
        $(By.xpath(xpathCheckBoxConfirm)).click();
        $(By.xpath(xpathSendTransferButton)).click();

        alert = switchTo().alert();

        assertTrue(alert.getText().contains("Successfully transferred $" + positiveTransfer + " to account " + userAccount2.getAccountNumber() + "!"));

        alert.accept();

        assertEquals(baseUrl + deshboardEndpoint, WebDriverRunner.url());
        $(By.xpath(xpathDashboard)).shouldBe(Condition.visible);

        assertEquals(getAccountById(userAccount2.getId()).getBalance(), positiveTransfer);
        assertEquals(getAccountById(userAccount1.getId()).getBalance(), amount - positiveTransfer);
    }

    @Test
    public void testNegativeTransfer() {
        $(By.xpath(xpathSelectAccount)).selectOptionContainingText(userAccount1.getAccountNumber());
        $(By.xpath(xpathRecipientName)).sendKeys(user.getUsername());
        $(By.xpath(xpathReicpientAccountNumber)).sendKeys(userAccount2.getAccountNumber());
        $(By.xpath(xpathAmount)).sendKeys(negativeTransfer.toString());
        $(By.xpath(xpathCheckBoxConfirm)).click();
        $(By.xpath(xpathSendTransferButton)).click();

        alert = switchTo().alert();

        assertTrue(alert.getText().contains("Error: Invalid transfer: insufficient funds or invalid accounts"));

        alert.accept();

        assertEquals(baseUrl + transferEndpoint, WebDriverRunner.url());
        $(By.xpath(xpathTransfer)).shouldBe(Condition.visible);

        assertTrue(Objects.requireNonNull($(".account-selector").getSelectedOptionText()).contains(amount.toString()));

        assertTrue($(By.xpath(xpathSelectAccount)).getText().contains(userAccount1.getAccountNumber()));
        assertEquals($(By.xpath(xpathRecipientName)).getValue(), user.getUsername());
        assertEquals($(By.xpath(xpathReicpientAccountNumber)).getValue(), userAccount2.getAccountNumber());
        assertEquals($(By.xpath(xpathAmount)).getValue(), negativeTransfer.toString());

        assertEquals(getAccountById(userAccount1.getId()).getBalance(), amount);
        assertEquals(getAccountById(userAccount2.getId()).getBalance(), 0.0);

    }
}
