package middle_api_autotest;

import org.assertj.core.api.SoftAssertions;
import org.example.UserRole;
import org.example.accounts.models.accounts.CreateUserAccountRequestDTO;
import org.example.accounts.models.accounts.CreateUserAccountResponseDTO;
import org.example.accounts.models.deposit.DepositRequestDTO;
import org.example.accounts.requests.accounts.CreateUserAccountRequest;
import org.example.accounts.requests.deposit.DepositRequest;
import org.example.admin.models.CreateUserRequestDTO;
import org.example.general_models.UserResponseDTO;
import org.example.admin.requests.CreateUserRequest;
import org.example.generators.RandomData;
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
    CreateUserAccountResponseDTO userAccount;

    @BeforeEach
    public void precondition() {
        softAssertions = new SoftAssertions();

        userRequestDTO = CreateUserRequestDTO.builder()
                .userName(RandomData.getRandomUserName())
                .password(RandomData.getRandomPassword())
                .role(UserRole.USER)
                .build();

        new CreateUserRequest(
                RequestSpecs.getAdminSpec(),
                ResponceSpecs.entityWasCreated())
                .post(userRequestDTO)
                .extract()
                .as(UserResponseDTO.class);

        userAccount = new CreateUserAccountRequest(RequestSpecs.getUserSpec(userRequestDTO.getUserName(), userRequestDTO.getPassword()),
                ResponceSpecs.entityWasCreated())
                .post(new CreateUserAccountRequestDTO())
                .extract()
                .as(CreateUserAccountResponseDTO.class);

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
        new DepositRequest(
                RequestSpecs.getUserSpec(userRequestDTO.getUserName(), userRequestDTO.getPassword()),
                ResponceSpecs.requestReturnsOk())
                .post(new DepositRequestDTO(userAccount.getId(), amount));

        var account = getAccountById(userAccount.getId());

        softAssertions.assertThat(account.getTransactions().size()).as("Количество транзакций не соответсвует ожидаемому").isEqualTo(1);
        softAssertions.assertThat(account.getBalance()).as("Баланс не соответствует ожидаемому").isEqualTo(amount);
    }

    @MethodSource("provaderNegativeDeposit")
    @ParameterizedTest
    public void testNegativeDeposit(Double amount) {

        new DepositRequest(
                RequestSpecs.getUserSpec(userRequestDTO.getUserName(), userRequestDTO.getPassword()),
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
