package api.senior_autotest;

import org.example.BankWidget;
import org.example.models.accoints.accounts.CreateAccountResponseDTO;
import org.example.models.accoints.deposit.DepositRequestDTO;
import org.example.models.admin.users.CreateUserResponseDTO;
import org.example.requests.skelethon.enams.Endpoint;
import org.example.requests.skelethon.requests.CrudRequest;
import org.example.specs.RequestSpecs;
import org.example.specs.ResponceSpecs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.example.BankWidget.deleteUser;
import static org.example.BankWidget.getAccountById;

public class DepositTest extends BaseTest{
    CreateAccountResponseDTO userAccount;

    CreateUserResponseDTO user;

    final static Double beginBalance = 0.0;
    final static Integer beginTransactions = 0;

    @BeforeEach
    public void precondition() {
        //по хорошему убрать бы эту строку и эти softAssertions вообще должны быть на других тестах, но посколько у меня их нет, то увы
        super.precondition();
        user = BankWidget.createUser();

        userAccount = BankWidget.createAccount(user);

        softAssertions.assertThat(userAccount.getBalance()).as("Аккаунт создался с балансом != 0").isEqualTo(beginTransactions);
        softAssertions.assertThat(userAccount.getTransactions().size()).as("Аккаунт создался с транзакцией").isEqualTo(beginBalance);
    }

    @AfterEach
    public void postcondition() {
        deleteUser(user);
    }

    @MethodSource("provaderPositiveDeposit")
    @ParameterizedTest
    public void testPositiveDeposit(Double amount) {
        new CrudRequest(
                RequestSpecs.getUserSpec(user.getUsername(), user.getPassword()),
                Endpoint.DEPOSIT,
                ResponceSpecs.requestReturnsOk())
                .post(new DepositRequestDTO(userAccount.getId(), amount));

        var account = getAccountById(userAccount.getId());

        softAssertions.assertThat(account.getTransactions().size()).as("Количество транзакций не соответсвует ожидаемому").isEqualTo(beginTransactions + 1);
        softAssertions.assertThat(account.getBalance()).as("Баланс не соответствует ожидаемому").isEqualTo(amount);
    }

    @MethodSource("provaderNegativeDeposit")
    @ParameterizedTest
    public void testNegativeDeposit(Double amount, String errorKey, String errorValue) {

        new CrudRequest(
                RequestSpecs.getUserSpec(user.getUsername(), user.getPassword()),
                Endpoint.DEPOSIT,
                ResponceSpecs.requestReturnsBadRequest(errorKey, errorValue))
                .post(new DepositRequestDTO(userAccount.getId(), amount));

        var account = getAccountById(userAccount.getId());

        softAssertions.assertThat(account.getTransactions().size()).as("Количество транзакций не соответсвует ожидаемому").isEqualTo(beginTransactions);
        softAssertions.assertThat(account.getBalance()).as("Баланс не соответствует ожидаемому").isEqualTo(beginBalance);
    }

    public static Stream<Arguments> provaderPositiveDeposit() {
        return Stream.of(
                Arguments.of( 100.0),
                Arguments.of( 5000.0)
        );
    }

    //в ответах нет message(без них тесты ходят нормально)
    public static Stream<Arguments> provaderNegativeDeposit() {
        return Stream.of(
                Arguments.of( 5001.0, "deposit", "Deposit amount cannot exceed 5000"),
                Arguments.of(0.0, "deposit", "Deposit amount must be at least 0.01"),
                Arguments.of(-1.0, "deposit", "Deposit amount must be at least 0.01")
        );
    }
}
