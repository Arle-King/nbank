package ui.senior_autotest;

import com.codeborne.selenide.Condition;
import org.example.api.models.accoints.accounts.CreateAccountResponseDTO;
import org.example.common.annotations.ApiVersion;
import org.example.common.annotations.UserSession;
import org.example.common.storage.SessionStorage;
import org.example.ui.enams.BankAlert;
import org.example.ui.pages.DepositPage;
import org.example.ui.pages.DeshboardPage;
import org.junit.jupiter.api.Test;
import ui.BaseUITest;

import static org.example.BankWidget.getAccountById;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DepositUITest extends BaseUITest {

    CreateAccountResponseDTO userAccount;

    Double banalce = Math.random() * 1000 / 10;

    @Test
    @UserSession
    @ApiVersion("with_deletion")
    public void testPositiveDeposit() {

        userAccount = SessionStorage.getSteps().createAccount().get(0);

        DeshboardPage page = new DepositPage()
                .open()
                .deposit(userAccount.getAccountNumber(), banalce)
                .checkAlertMassageAndAccept(BankAlert.DEPOSIT_SUCCESS, banalce, userAccount.getAccountNumber())
                .getPage(DeshboardPage.class);

        page.getDashboard().shouldBe(Condition.visible);

        assertEquals(getAccountById(userAccount.getId()).getBalance(), banalce);
    }

    @Test
    @UserSession
    @ApiVersion("with_deletion")
    public void testNegativeDeposit() {
        banalce+= 5000;

        userAccount = SessionStorage.getSteps().createAccount().get(0);

        DepositPage page = new DepositPage()
                .open()
                .deposit(userAccount.getAccountNumber(), banalce)
                .checkAlertMassageAndAccept(BankAlert.DEPOSIT_LESS_5000);

        assertTrue(page.getSelectAccount().getSelectedOptionText().contains(userAccount.getAccountNumber()));
        assertEquals(page.getEnterAmount().getValue(), banalce.toString());

        assertEquals(getAccountById(userAccount.getId()).getBalance(), userAccount.getBalance());
    }
}
