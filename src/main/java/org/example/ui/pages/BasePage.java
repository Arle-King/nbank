package org.example.ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.example.api.models.admin.users.CreateUserRequestDTO;
import org.example.api.specs.RequestSpecs;
import org.example.ui.elements.BaseElement;
import org.example.ui.enams.BankAlert;
import org.openqa.selenium.Alert;

import java.util.List;
import java.util.function.Function;

import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.switchTo;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class BasePage<T extends BasePage> {
    public abstract String endpoint();

    public T open() {
        return Selenide.open(endpoint(), (Class<T>) this.getClass());
    }

    public <T extends BasePage> T getPage(Class<T> pageClass) {
        return Selenide.page(pageClass);
    }

    public T checkAlertMassageAndAccept(BankAlert bankAlert) {
        Alert alert = switchTo().alert();

        assertThat(alert.getText()).contains(bankAlert.getMassage());

        alert.accept();

        return (T) this;
    }

    public T checkAlertMassageAndAccept(BankAlert bankAlert, Object... args) {
        Alert alert = switchTo().alert();
        String expectedMessage = bankAlert.format(args);
        assertThat(alert.getText()).contains(expectedMessage);
        alert.accept();
        return (T) this;
    }

    public String getUrl() {
        return WebDriverRunner.url();
    }

    public static void authAsUser(String username, String password) {
        Selenide.open("/");
        String userAuthHeader = RequestSpecs.getAuthUserToken(username, password);
        executeJavaScript("localStorage.setItem('authToken', arguments[0])", userAuthHeader);
    }

    public static void authAsUser(CreateUserRequestDTO user) {
        authAsUser(user.getUsername(), user.getPassword());
    }

    protected <T extends BaseElement> List<T> generatePageElement(ElementsCollection elementsCollection, Function<SelenideElement, T> constructor) {
        return elementsCollection.stream().map(constructor).toList();
    }
}
