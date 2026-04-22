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
import org.example.ui.pages.DeshboardPage;
import org.example.ui.pages.TransferPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.BaseUITest;

import static org.example.BankWidget.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransferUITest extends BaseUITest {

    CreateUserRequestDTO user;

    CreateAccountResponseDTO userAccount1;
    CreateAccountResponseDTO userAccount2;


    Double amount = Math.random() * 9999 + 1;
    Double positiveTransfer = amount * 0.9;
    Double negativeTransfer = amount * 1.1;

    @BeforeEach
    public void precondition() {
        user = RandomModelGenerator.generate(CreateUserRequestDTO.class);

        new CrudRequest(RequestSpecs.getAdminSpec(),
                Endpoint.CREATE_USER,
                ResponceSpecs.entityWasCreated())
                .post(user)
                .extract()
                .header("Authorization");

        userAccount1 = BankWidget.createAccount(user.getUsername(), user.getPassword());
        userAccount2 = BankWidget.createAccount(user.getUsername(), user.getPassword());

        dodepInAccount(user.getUsername(), user.getPassword(), userAccount1.getId(), amount);

        authAsUser(user);
    }

    @AfterEach
    public void postcondition() {
        deleteUser(user.getUsername());
    }

    @Test
    public void testPositiveTransfer() {
        DeshboardPage page = new TransferPage()
                .open()
                .transfer(userAccount1, user, userAccount2, positiveTransfer)
                .checkAlertMassageAndAccept(BankAlert.MAKE_A_TRANSFER_SUCCESS, positiveTransfer, userAccount2.getAccountNumber())
                .getPage(DeshboardPage.class);

        page.getDashboard().shouldBe(Condition.visible);

        assertEquals(getAccountById(userAccount2.getId()).getBalance(), positiveTransfer);
        assertEquals(getAccountById(userAccount1.getId()).getBalance(), amount - positiveTransfer);
    }

    @Test
    public void testNegativeTransfer() {
        TransferPage page = new TransferPage()
                .open()
                .transfer(userAccount1, user, userAccount2, negativeTransfer)
                .checkAlertMassageAndAccept(BankAlert.MAKE_A_TRANSFER_ERROR);


        page.getTransferWelcomeText().shouldBe(Condition.visible);

        assertTrue(page.getSelectAccount().getSelectedOptionText().contains(amount.toString()));
        assertEquals(page.getRecipientName().getValue(), user.getUsername());
        assertEquals(page.getReicpientAccountNumber().getValue(), userAccount2.getAccountNumber());
        assertEquals(page.getAmount().getValue(), negativeTransfer.toString());
        assertTrue(page.getCheckBoxConfirm().isDisplayed());

        assertEquals(getAccountById(userAccount1.getId()).getBalance(), amount);
        assertEquals(getAccountById(userAccount2.getId()).getBalance(), 0.0);

    }
}
