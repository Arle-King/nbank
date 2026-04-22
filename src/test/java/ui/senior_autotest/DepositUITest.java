package ui.senior_autotest;

import com.codeborne.selenide.Condition;
import org.example.api.models.accoints.accounts.CreateAccountResponseDTO;
import org.example.common.annotations.UserSession;
import org.example.common.storage.SessionStorage;
import org.example.ui.enams.BankAlert;
import org.example.ui.pages.DepositPage;
import org.example.ui.pages.DeshboardPage;
import org.junit.jupiter.api.Test;
import ui.BaseUITest;

import java.util.List;

import static org.example.BankWidget.getAccountById;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DepositUITest extends BaseUITest {

    List<CreateAccountResponseDTO> userAccount;

    Double banalce = Math.random() * 1000 / 10;

    @Test
    @UserSession
    public void testPositiveDeposit() {

        userAccount = SessionStorage.getSteps().createAccount();

        DeshboardPage page = new DepositPage()
                .open()
                .deposit(userAccount.get(0).getAccountNumber(), banalce)
                .checkAlertMassageAndAccept(BankAlert.DEPOSIT_SUCCESS, banalce, userAccount.get(0).getAccountNumber())
                .getPage(DeshboardPage.class);

        page.getDashboard().shouldBe(Condition.visible);

        assertEquals(getAccountById(userAccount.get(0).getId()).getBalance(), banalce);
    }

    @Test
    @UserSession
    public void testNegativeDeposit() {
        banalce+= 5000;

        userAccount = SessionStorage.getSteps().createAccount();

        DepositPage page = new DepositPage()
                .open()
                .deposit(userAccount.get(0).getAccountNumber(), banalce)
                .checkAlertMassageAndAccept(BankAlert.DEPOSIT_LESS_5000);

        assertTrue(page.getSelectAccount().getSelectedOptionText().contains(userAccount.get(0).getAccountNumber()));
        assertEquals(page.getEnterAmount().getValue(), banalce.toString());

        assertEquals(getAccountById(userAccount.get(0).getId()).getBalance(), userAccount.get(0).getBalance());
    }
}
