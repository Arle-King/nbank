package ui;

import api.BaseTest;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.example.api.configs.Config;
import org.example.api.models.admin.users.CreateUserRequestDTO;
import org.example.api.specs.RequestSpecs;
import org.junit.jupiter.api.BeforeAll;

import java.util.Map;

import static com.codeborne.selenide.Selenide.executeJavaScript;

public class BaseUITest extends BaseTest {
    @BeforeAll
    public static void setupSelenoid() {
        Configuration.remote = Config.getProperty("ui.remote");
        Configuration.baseUrl = Config.getProperty("ui.baseUrl");
        Configuration.browser = Config.getProperty("ui.browser");
        Configuration.browserSize = Config.getProperty("ui.browserSize");

        Configuration.browserCapabilities.setCapability("selenoid:options",
                Map.of("enableVNC", true, "enableLog", true)
        );
    }
    public void authAsUser(String username, String password) {
        Selenide.open("/");
        String userAuthHeader = RequestSpecs.getAuthUserToken(username, password);
        executeJavaScript("localStorage.setItem('authToken', arguments[0])", userAuthHeader);
    }

    public void authAsUser(CreateUserRequestDTO user) {
        authAsUser(user.getUsername(), user.getPassword());
    }
}
