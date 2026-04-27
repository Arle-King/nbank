package ui.middleUITest;

import com.codeborne.selenide.Condition;
import org.example.api.generators.RandomModelGenerator;
import org.example.api.models.admin.users.CreateUserRequestDTO;
import org.example.api.models.customer.profile.UpdateProfileRequestDTO;
import org.example.api.skelethon.enams.Endpoint;
import org.example.api.skelethon.requests.CrudRequest;
import org.example.api.specs.RequestSpecs;
import org.example.api.specs.ResponceSpecs;
import org.example.ui.enams.BankAlert;
import org.example.ui.pages.CustomerNamePage;
import org.example.ui.pages.DeshboardPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.BaseUITest;

import static org.example.BankWidget.deleteUser;
import static org.example.BankWidget.getUserByUsername;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerNameUITest extends BaseUITest {

    CreateUserRequestDTO user;
    String newName = RandomModelGenerator.generate(UpdateProfileRequestDTO.class).getName();
    final String welcomeText = "Welcome, " + newName + "!";

    @BeforeEach
    public void precondition() {
        user = RandomModelGenerator.generate(CreateUserRequestDTO.class);

        new CrudRequest(RequestSpecs.getAdminSpec(),
                Endpoint.CREATE_USER,
                ResponceSpecs.entityWasCreated())
                .post(user)
                .extract()
                .header(ResponceSpecs.AUTH_HEADER);

        authAsUser(user);
    }

    @AfterEach
    public void postcondition() {
        deleteUser(user.getUsername());
    }

    @Test
    public void testPositiveCustomerName() {
        DeshboardPage page = new CustomerNamePage()
                .open()
                .editName(newName)
                .checkAlertMassageAndAccept(BankAlert.CUSTOM_NAME_SUCCESS)
                .getPage(DeshboardPage.class);

        page.getDashboard().shouldBe(Condition.visible);

        assertEquals(page.getWelcomeText().getText(), welcomeText);
        assertEquals(page.getUsername().getText(), newName);

        assertEquals(getUserByUsername(user.getUsername()).getName(), newName);
    }

    @Test
    public void testegativeCustomerName() {
        newName = newName + "!";

        CustomerNamePage page = new CustomerNamePage()
                .open()
                .editName(newName)
                .checkAlertMassageAndAccept(BankAlert.CUSTOM_NAME_ERROR);

        page.getEditProfile().shouldBe(Condition.visible);
        assertEquals(page.getFieldEditNewName().getValue(), newName);
    }
}
