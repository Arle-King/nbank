package middle_api_autotest;

import org.assertj.core.api.SoftAssertions;
import org.example.BankWidget;
import org.example.UserRole;
import org.example.accounts.models.accounts.CreateUserAccountRequestDTO;
import org.example.accounts.models.accounts.CreateUserAccountResponseDTO;
import org.example.accounts.models.transfer.TransferRequestDTO;
import org.example.accounts.requests.accounts.CreateUserAccountRequest;
import org.example.accounts.requests.transfer.TransferRequest;
import org.example.admin.models.CreateUserRequestDTO;
import org.example.admin.requests.CreateUserRequest;
import org.example.general_models.UserResponseDTO;
import org.example.generators.RandomData;
import org.example.specs.RequestSpecs;
import org.example.specs.ResponceSpecs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.example.BankWidget.getAccountById;

public class TransferTest {

    SoftAssertions softAssertions;
    CreateUserRequestDTO userRequest1;
    List<CreateUserAccountResponseDTO> userAccounts;

    @BeforeEach
    public void precondition() {
        softAssertions = new SoftAssertions();
        UserResponseDTO user1;
        UserResponseDTO user2;
        CreateUserRequestDTO userRequest2;

        userRequest1 = CreateUserRequestDTO.builder()
                .userName(RandomData.getRandomUserName())
                .password(RandomData.getRandomPassword())
                .role(UserRole.USER)
                .build();

        userRequest2 = CreateUserRequestDTO.builder()
                .userName(RandomData.getRandomUserName())
                .password(RandomData.getRandomPassword())
                .role(UserRole.USER)
                .build();

        user1 = new CreateUserRequest(
                RequestSpecs.getAdminSpec(),
                ResponceSpecs.entityWasCreated())
                .post(userRequest1)
                .extract()
                .as(UserResponseDTO.class);

        user2 = new CreateUserRequest(
                RequestSpecs.getAdminSpec(),
                ResponceSpecs.entityWasCreated())
                .post(userRequest2)
                .extract()
                .as(UserResponseDTO.class);

        userAccounts = new ArrayList<>();
        userAccounts.add(new CreateUserAccountRequest(RequestSpecs.getUserSpec(userRequest1.getUserName(), userRequest1.getPassword()),
                ResponceSpecs.entityWasCreated())
                .post(new CreateUserAccountRequestDTO())
                .extract()
                .as(CreateUserAccountResponseDTO.class));

        userAccounts.add(new CreateUserAccountRequest(RequestSpecs.getUserSpec(userRequest1.getUserName(), userRequest1.getPassword()),
                ResponceSpecs.entityWasCreated())
                .post(new CreateUserAccountRequestDTO())
                .extract()
                .as(CreateUserAccountResponseDTO.class));

        userAccounts.add(new CreateUserAccountRequest(RequestSpecs.getUserSpec(userRequest2.getUserName(), userRequest2.getPassword()),
                ResponceSpecs.entityWasCreated())
                .post(new CreateUserAccountRequestDTO())
                .extract()
                .as(CreateUserAccountResponseDTO.class));

        user1 = BankWidget.getUresById(user1.getId());
        user2 = BankWidget.getUresById(user2.getId());

        softAssertions.assertThat(user1.getAccounts().size()).as("У user неверное кол-во account").isEqualTo(2);
        softAssertions.assertThat(user1.getAccounts().get(0).getBalance()).as("Баланс не совпадает с ожидаемым").isEqualTo(0.0);
        softAssertions.assertThat(user1.getAccounts().get(0).getTransactions().size()).as("Аккаунт создался с транзакцией").isEqualTo(0);

        softAssertions.assertThat(user1.getAccounts().get(1).getBalance()).as("Баланс не совпадает с ожидаемым").isEqualTo(0.0);
        softAssertions.assertThat(user1.getAccounts().get(1).getTransactions().size()).as("Аккаунт создался с транзакцией").isEqualTo(0);



        softAssertions.assertThat(user2.getAccounts().size()).as("У user неверное кол-во account").isEqualTo(1);
        softAssertions.assertThat(user2.getAccounts().get(0).getBalance()).as("Баланс не совпадает с ожидаемым").isEqualTo(0.0);
        softAssertions.assertThat(user2.getAccounts().get(0).getTransactions().size()).as("Аккаунт создался с транзакцией").isEqualTo(0);

    }

    @AfterEach
    public void postcondition() {
        softAssertions.assertAll();
    }

    @ParameterizedTest
    @MethodSource("provaderPositiveTransfer")
    public void positiveTransferTest(Double balance, int receiverAccountId, Double amount) {
        BankWidget.dodepInAccount(userRequest1.getUserName(), userRequest1.getPassword(), userAccounts.get(0).getId(), balance);

        TransferRequestDTO transferRequestDTO = TransferRequestDTO.builder()
                .senderAccountId(userAccounts.get(0).getId())
                .receiverAccountId(userAccounts.get(receiverAccountId).getId())
                .amount(amount)
                .build();

        int startCount = getAccountById(userAccounts.get(0).getId()).getTransactions().size();

        new TransferRequest(
                RequestSpecs.getUserSpec(userRequest1.getUserName(), userRequest1.getPassword()),
                ResponceSpecs.requestReturnsOk())
                .post(transferRequestDTO);

        softAssertions.assertThat(getAccountById(userAccounts.get(0).getId()).getBalance()).as("Баланс не совпадает с ожидаемым").isEqualTo(balance - amount);
        softAssertions.assertThat(getAccountById(userAccounts.get(0).getId()).getTransactions().size()).as("Количество транзакций не совпадает с ожидаемым").isEqualTo(startCount + 1);


        softAssertions.assertThat(getAccountById(userAccounts.get(receiverAccountId).getId()).getBalance()).as("Баланс не совпадает с ожидаемым").isEqualTo(amount);
        softAssertions.assertThat(getAccountById(userAccounts.get(receiverAccountId).getId()).getTransactions().size()).as("Количество транзакций не совпадает с ожидаемым").isEqualTo(1);
    }

    @ParameterizedTest
    @MethodSource("provaderNegativeTransfer")
    public void negativeTransferTest(Double balance, int receiverAccountId, Double amount) {
        BankWidget.dodepInAccount(userRequest1.getUserName(), userRequest1.getPassword(), userAccounts.get(0).getId(), balance);

        TransferRequestDTO transferRequestDTO = TransferRequestDTO.builder()
                .senderAccountId(userAccounts.get(0).getId())
                .receiverAccountId(userAccounts.get(receiverAccountId).getId())
                .amount(amount)
                .build();

        int startCount = getAccountById(userAccounts.get(0).getId()).getTransactions().size();

        new TransferRequest(
                RequestSpecs.getUserSpec(userRequest1.getUserName(), userRequest1.getPassword()),
                ResponceSpecs.requestReturnsBadRequest())
                .post(transferRequestDTO);

        softAssertions.assertThat(getAccountById(userAccounts.get(0).getId()).getBalance()).as("Баланс не совпадает с ожидаемым").isEqualTo(balance);
        softAssertions.assertThat(getAccountById(userAccounts.get(0).getId()).getTransactions().size()).as("Количество транзакций не совпадает с ожидаемым").isEqualTo(startCount);


        softAssertions.assertThat(getAccountById(userAccounts.get(receiverAccountId).getId()).getBalance()).as("Баланс не совпадает с ожидаемым").isEqualTo(0.0);
        softAssertions.assertThat(getAccountById(userAccounts.get(receiverAccountId).getId()).getTransactions().size()).as("Количество транзакций не совпадает с ожидаемым").isEqualTo(0);
    }

    @Test
    public void nonExistAccountTransfer() {
        Double balance = 2000.0;
        BankWidget.dodepInAccount(userRequest1.getUserName(), userRequest1.getPassword(), userAccounts.get(0).getId(), balance);

        TransferRequestDTO transferRequestDTO = TransferRequestDTO.builder()
                .senderAccountId(userAccounts.get(0).getId())
                .receiverAccountId(0)
                .amount(1500.0)
                .build();

        int startCount = getAccountById(userAccounts.get(0).getId()).getTransactions().size();

        new TransferRequest(
                RequestSpecs.getUserSpec(userRequest1.getUserName(), userRequest1.getPassword()),
                ResponceSpecs.requestReturnsBadRequest())
                .post(transferRequestDTO);

        softAssertions.assertThat(getAccountById(userAccounts.get(0).getId()).getBalance()).as("Баланс не совпадает с ожидаемым").isEqualTo(balance);
        softAssertions.assertThat(getAccountById(userAccounts.get(0).getId()).getTransactions().size()).as("Количество транзакций не совпадает с ожидаемым").isEqualTo(startCount);
    }

    // 0 - основной счёт, 1 - доп счёт, 2 - чужой счёт
    public static Stream<Arguments> provaderPositiveTransfer() {
        return Stream.of(
                Arguments.of(10100.0, 1, 10000.0), // макс перевод себе
                Arguments.of(10100.0, 2, 10000.0)  // макс перевод на чужой счёт
        );
    }

    public static Stream<Arguments> provaderNegativeTransfer() {
        return Stream.of(
                Arguments.of( 100.0, 1, 0.0), // перевод 0 денег
                Arguments.of( 1000.0, 1, -100.0), // перевод отрицательного кол-ва денег
                Arguments.of( 10100.0, 1, 10050.0), // выход за пределы допустимого перевода
                Arguments.of( 4000.0, 1, 4500.0) // перевод без необходимой суммы на счету
        );
    }
}
