package ui.junior_autotest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.example.api.generators.RandomModelGenerator;
import org.example.api.models.admin.users.CreateUserRequestDTO;
import org.example.api.models.customer.profile.UpdateProfileRequestDTO;
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

import static com.codeborne.selenide.Selenide.*;
import static org.example.BankWidget.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerNameUITest {

    CreateUserRequestDTO user;

    static final String baseUrl = "http://192.168.0.137:3000";
    static final String loginEndpoint = "/login";
    static final String editProfileEndpoint = "/edit-profile";
    static final String deshboardEndpoint = "/dashboard";

    String xpathDashboard = "//*[contains(text(), 'User Dashboard')]";
    String xpathEditProfile = "//*[contains(text(), 'Edit Profile')]";
    String xpathWelcomeText = "//*[contains(text(), 'Welcome, ')]";
    String xpathUsername = "//span[@class='user-name']";
    String xpathFieldEditNewName = "//input[@placeholder = 'Enter new name']";
    String xpathSaveButton = "//button[contains(text(), 'Save Changes')]";

    String newName = RandomModelGenerator.generate(UpdateProfileRequestDTO.class).getName();

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
                .header("Authorization");

        Selenide.open(loginEndpoint);

        executeJavaScript("localStorage.setItem('authToken', arguments[0])", token);

        Selenide.open(editProfileEndpoint);
    }

    @AfterEach
    public void postcondition() {
        deleteUser(user.getUsername());
    }

    @Test
    public void testPositiveCustomerName() {
        $(By.xpath(xpathFieldEditNewName)).sendKeys(newName);
        $(By.xpath(xpathSaveButton)).click();

        alert = switchTo().alert();

        assertTrue(alert.getText().contains("Name updated successfully!"));

        alert.accept();

        assertEquals(baseUrl + deshboardEndpoint, WebDriverRunner.url());
        $(By.xpath(xpathDashboard)).shouldBe(Condition.visible);

        assertEquals($(By.xpath(xpathWelcomeText)).getText(), "Welcome, " + newName + "!");
        assertEquals($(By.xpath(xpathUsername)).getText(), newName);

        assertEquals(getUserByUsername(user.getUsername()).getName(), newName);
    }

    @Test
    public void testegativeCustomerName() {
        newName = newName + "!";
        $(By.xpath(xpathFieldEditNewName)).sendKeys(newName);
        $(By.xpath(xpathSaveButton)).click();

        alert = switchTo().alert();

        assertTrue(alert.getText().contains("Name must contain two words with letters only"));

        alert.accept();

        assertEquals(baseUrl + editProfileEndpoint, WebDriverRunner.url());
        $(By.xpath(xpathEditProfile)).shouldBe(Condition.visible);
        assertEquals($(By.xpath(xpathFieldEditNewName)).getValue(), newName);

    }

}
