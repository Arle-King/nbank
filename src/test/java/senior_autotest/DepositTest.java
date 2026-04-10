package senior_autotest;

import org.assertj.core.api.SoftAssertions;
import org.example.generators.RandomData;
import org.example.models.accoints.accounts.CreateAccountRequestDTO;
import org.example.models.accoints.accounts.CreateAccountResponseDTO;
import org.example.models.accoints.deposit.DepositRequestDTO;
import org.example.models.admin.users.CreateUserRequestDTO;
import org.example.requests.skelethon.requests.CrudRequest;
import org.example.requests.skelethon.enams.Endpoint;
import org.example.requests.skelethon.enams.Role;
import org.example.requests.skelethon.requests.ValidatedCrudRequest;
import org.example.specs.RequestSpecs;
import org.example.specs.ResponceSpecs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.example.BankWidget.getAccountById;

public class DepositTest {
    SoftAssertions softAssertions;
    CreateUserRequestDTO userRequestDTO;
    CreateAccountResponseDTO userAccount;

    @BeforeEach
    public void precondition() {
        softAssertions = new SoftAssertions();

        userRequestDTO = CreateUserRequestDTO.builder()
                .username(RandomData.getRandomUserName())
                .password(RandomData.getRandomPassword())
                .role(Role.USER)
                .build();

        new CrudRequest(
                RequestSpecs.getAdminSpec(),
                Endpoint.CREATE_USER,
                ResponceSpecs.entityWasCreated())
                .post(userRequestDTO);

        userAccount = new ValidatedCrudRequest<CreateAccountResponseDTO>(RequestSpecs.getUserSpec(userRequestDTO.getUsername(), userRequestDTO.getPassword()),
                Endpoint.CREATE_ACCOUNT,
                ResponceSpecs.entityWasCreated())
                .post(new CreateAccountRequestDTO());

        softAssertions.assertThat(userAccount.getBalance()).as("Аккаунт создался с балансом != 0").isEqualTo(0.0);
        softAssertions.assertThat(userAccount.getTransactions().size()).as("Аккаунт создался с транзакцией").isEqualTo(0);
    }

    @AfterEach
    public void postcondition() {
        //было бы круто удалять тестовые данные да...
        softAssertions.assertAll();
    }

    @MethodSource("provaderPositiveDeposit")
    @ParameterizedTest
    public void testPositiveDeposit(Double amount) {
        new CrudRequest(
                RequestSpecs.getUserSpec(userRequestDTO.getUsername(), userRequestDTO.getPassword()),
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
                RequestSpecs.getUserSpec(userRequestDTO.getUsername(), userRequestDTO.getPassword()),
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
