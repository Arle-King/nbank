package ui.senior_autotest;

import com.codeborne.selenide.Condition;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.api.generators.RandomModelGenerator;
import org.example.api.models.admin.users.CreateUserRequestDTO;
import org.example.api.models.customer.profile.UpdateProfileRequestDTO;
import org.example.common.annotations.UserSession;
import org.example.common.storage.SessionStorage;
import org.example.ui.enams.BankAlert;
import org.example.ui.pages.CustomerNamePage;
import org.example.ui.pages.DeshboardPage;
import org.junit.jupiter.api.Test;
import ui.BaseUITest;

import static org.example.BankWidget.getUserByUsername;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CustomerNameUITest extends BaseUITest {

    CreateUserRequestDTO user;
    final String newName = RandomModelGenerator.generate(UpdateProfileRequestDTO.class).getName();
    String welcomeTextNew = DeshboardPage.welcomeTextNewname.replace("noname", newName);
    final String newErrorName = RandomModelGenerator.generate(UpdateProfileRequestDTO.class).getName() + RandomStringUtils.random(1, "!@#$%^&*1234567890");

    @Test
    @UserSession
    public void testPositiveCustomerName() {

        user = SessionStorage.getUser();

        DeshboardPage page = new CustomerNamePage()
                .open()
                .editName(newName)
                .checkAlertMassageAndAccept(BankAlert.CUSTOM_NAME_SUCCESS)
                .getPage(DeshboardPage.class);

        page.getDashboard().shouldBe(Condition.visible);

        assertEquals(page.getWelcomeText().getText(), welcomeTextNew);
        assertEquals(page.getUsername().getText(), newName);

        assertEquals(getUserByUsername(user.getUsername()).getName(), newName);
    }

    @Test
    @UserSession
    public void testegativeCustomerName() {
        CustomerNamePage page = new CustomerNamePage()
                .open()
                .editName(newErrorName)
                .checkAlertMassageAndAccept(BankAlert.CUSTOM_NAME_ERROR);

        page.getEditProfile().shouldBe(Condition.visible);
        assertEquals(page.getFieldEditNewName().getValue(), newErrorName);

        assertNotEquals(getUserByUsername(user.getUsername()).getName(), newName);
    }
}
