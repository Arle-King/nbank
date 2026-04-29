package ui.senior_autotest;

import com.codeborne.selenide.Condition;
import org.example.api.models.accoints.accounts.CreateAccountResponseDTO;
import org.example.api.models.admin.users.CreateUserRequestDTO;
import org.example.common.annotations.UserSession;
import org.example.common.storage.SessionStorage;
import org.example.ui.enams.BankAlert;
import org.example.ui.pages.DeshboardPage;
import org.example.ui.pages.TransferPage;
import org.junit.jupiter.api.Test;
import ui.BaseUITest;

import java.util.List;

import static org.example.BankWidget.dodepInAccount;
import static org.example.BankWidget.getAccountById;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransferUITest extends BaseUITest {

    CreateUserRequestDTO user;

    List<CreateAccountResponseDTO> userAccounts;

    Double amount = Math.random() * 4999 + 1;
    Double positiveTransfer = amount * 0.9;
    Double negativeTransfer = amount * 1.1;

    @Test
    @UserSession
    public void testPositiveTransfer() {
        
        user = SessionStorage.getUser();
        userAccounts = SessionStorage.getSteps().createAccount(2);
        
        dodepInAccount(user.getUsername(), user.getPassword(), userAccounts.get(0).getId(), amount);
        
        
        DeshboardPage page = new TransferPage()
                .open()
                .transfer(userAccounts.get(0), user, userAccounts.get(1), positiveTransfer)
                .checkAlertMassageAndAccept(BankAlert.MAKE_A_TRANSFER_SUCCESS, positiveTransfer, userAccounts.get(1).getAccountNumber())
                .getPage(DeshboardPage.class);

        page.getDashboard().shouldBe(Condition.visible);

        assertEquals(getAccountById(userAccounts.get(1).getId()).getBalance(), positiveTransfer);
        assertEquals(getAccountById(userAccounts.get(0).getId()).getBalance(), amount - positiveTransfer);
    }

    @Test
    @UserSession
    public void testNegativeTransfer() {

        user = SessionStorage.getUser();
        userAccounts = SessionStorage.getSteps().createAccount(2);

        dodepInAccount(user.getUsername(), user.getPassword(), userAccounts.get(0).getId(), amount);

        TransferPage page = new TransferPage()
                .open()
                .transfer(userAccounts.get(0), user, userAccounts.get(1), negativeTransfer)
                .checkAlertMassageAndAccept(BankAlert.MAKE_A_TRANSFER_ERROR);


        page.getTransferWelcomeText().shouldBe(Condition.visible);

        //Выводить всю страницу в элемент большого смысла не вижу. 100/100 будут более важные задачи
        assertTrue(page.getSelectAccount().getSelectedOptionText().contains(amount.toString()));
        assertEquals(page.getRecipientName().getValue(), user.getUsername());
        assertEquals(page.getReicpientAccountNumber().getValue(), userAccounts.get(1).getAccountNumber());
        assertEquals(page.getAmount().getValue(), negativeTransfer.toString());
        assertTrue(page.getCheckBoxConfirm().isDisplayed());

        assertEquals(getAccountById(userAccounts.get(0).getId()).getBalance(), amount);
        assertEquals(getAccountById(userAccounts.get(1).getId()).getBalance(), 0.0);

    }
}
