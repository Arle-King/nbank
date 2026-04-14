package senior_autotest;

import org.assertj.core.api.SoftAssertions;
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

public class DepositTest {
    SoftAssertions softAssertions;
    CreateAccountResponseDTO userAccount;

    CreateUserResponseDTO user;

    @BeforeEach
    public void precondition() {
        softAssertions = new SoftAssertions();

        user = BankWidget.createUser();

        userAccount = BankWidget.createAccount(user);

        softAssertions.assertThat(userAccount.getBalance()).as("Аккаунт создался с балансом != 0").isEqualTo(0.0);
        softAssertions.assertThat(userAccount.getTransactions().size()).as("Аккаунт создался с транзакцией").isEqualTo(0);
    }

    @AfterEach
    public void postcondition() {
        deleteUser(user);
        softAssertions.assertAll();
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

        softAssertions.assertThat(account.getTransactions().size()).as("Количество транзакций не соответсвует ожидаемому").isEqualTo(1);
        softAssertions.assertThat(account.getBalance()).as("Баланс не соответствует ожидаемому").isEqualTo(amount);
    }

    @MethodSource("provaderNegativeDeposit")
    @ParameterizedTest
    public void testNegativeDeposit(Double amount) {

        new CrudRequest(
                RequestSpecs.getUserSpec(user.getUsername(), user.getPassword()),
                Endpoint.DEPOSIT,
                ResponceSpecs.requestReturnsBadRequest())
                .post(new DepositRequestDTO(userAccount.getId(), amount));

        var account = getAccountById(userAccount.getId());

        softAssertions.assertThat(account.getTransactions().size()).as("Количество транзакций не соответсвует ожидаемому").isEqualTo(0);
        softAssertions.assertThat(account.getBalance()).as("Баланс не соответствует ожидаемому").isEqualTo(0.0);
    }

    public static Stream<Arguments> provaderPositiveDeposit() {
        return Stream.of(
                Arguments.of( 100.0),
                Arguments.of( 5000.0)
        );
    }

    public static Stream<Arguments> provaderNegativeDeposit() {
        return Stream.of(
                Arguments.of( 5001.0),
                Arguments.of(0.0),
                Arguments.of(-1.0)
        );
    }
}
