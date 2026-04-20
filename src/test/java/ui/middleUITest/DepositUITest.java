package ui.middleUITest;

import com.codeborne.selenide.Condition;
import org.example.BankWidget;
import org.example.api.generators.RandomModelGenerator;
import org.example.api.models.accoints.accounts.CreateAccountResponseDTO;
import org.example.api.models.admin.users.CreateUserRequestDTO;
import org.example.api.skelethon.enams.Endpoint;
import org.example.api.skelethon.requests.CrudRequest;
import org.example.api.specs.RequestSpecs;
import org.example.api.specs.ResponceSpecs;
import org.example.ui.enams.BankAlert;
import org.example.ui.pages.DepositPage;
import org.example.ui.pages.DeshboardPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.BaseUITest;

import static org.example.BankWidget.deleteUser;
import static org.example.BankWidget.getAccountById;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DepositUITest extends BaseUITest {

    CreateUserRequestDTO user;

    CreateAccountResponseDTO userAccount;

    Double banalce = Math.random() * 1000 / 10;

    @BeforeEach
    public void precondition() {

        user = RandomModelGenerator.generate(CreateUserRequestDTO.class);

        new CrudRequest(RequestSpecs.getAdminSpec(),
                Endpoint.CREATE_USER,
                ResponceSpecs.entityWasCreated())
                .post(user)
                .extract()
                .header("Authorization");

        userAccount = BankWidget.createAccount(user.getUsername(), user.getPassword());

        authAsUser(user);
    }

    @AfterEach
    public void postcondition() {
        deleteUser(user.getUsername());
    }

    @Test
    public void testPositiveDeposit() {

        DeshboardPage page = new DepositPage()
                .open()
                .deposit(userAccount.getAccountNumber(), banalce)
                .checkAlertMassageAndAccept(BankAlert.DEPOSIT_SUCCESS, banalce, userAccount.getAccountNumber())
                .getPage(DeshboardPage.class);

        page.getDashboard().shouldBe(Condition.visible);

        assertEquals(getAccountById(userAccount.getId()).getBalance(), banalce);
    }

    @Test
    public void testNegativeDeposit() {
        banalce+= 5000;

        DepositPage page = new DepositPage()
                .open()
                .deposit(userAccount.getAccountNumber(), banalce)
                .checkAlertMassageAndAccept(BankAlert.DEPOSIT_LESS_5000);

        assertTrue(page.getSelectAccount().getSelectedOptionText().contains(userAccount.getAccountNumber()));
        assertEquals(page.getEnterAmount().getValue(), banalce.toString());

        assertEquals(getAccountById(userAccount.getId()).getBalance(), userAccount.getBalance());
    }
}
