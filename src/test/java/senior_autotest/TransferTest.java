package senior_autotest;

import org.assertj.core.api.SoftAssertions;
import org.example.BankWidget;
import org.example.models.accoints.accounts.CreateAccountResponseDTO;
import org.example.models.accoints.transfer.TransferRequestDTO;
import org.example.models.admin.users.CreateUserResponseDTO;
import org.example.requests.skelethon.enams.Endpoint;
import org.example.requests.skelethon.requests.CrudRequest;
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

    CreateUserResponseDTO user1;
    CreateUserResponseDTO user2;
    List<CreateAccountResponseDTO> userAccounts;

    @BeforeEach
    public void precondition() {
        softAssertions = new SoftAssertions();

        user1 = BankWidget.createUser();
        user2 = BankWidget.createUser();

        userAccounts = new ArrayList<>();
        userAccounts.add(BankWidget.createAccount(user1));
        userAccounts.add(BankWidget.createAccount(user1));
        userAccounts.add(BankWidget.createAccount(user2));

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
        BankWidget.deleteUser(user1);
        BankWidget.deleteUser(user2);
        softAssertions.assertAll();
    }

    @ParameterizedTest
    @MethodSource("provaderPositiveTransfer")
    public void positiveTransferTest(Double balance, int receiverAccountId, Double amount) {
        BankWidget.dodepInAccount(user1.getUsername(), user1.getPassword(), userAccounts.get(0).getId(), balance);

        TransferRequestDTO transferRequestDTO = TransferRequestDTO.builder()
                .senderAccountId(userAccounts.get(0).getId())
                .receiverAccountId(userAccounts.get(receiverAccountId).getId())
                .amount(amount)
                .build();

        int startCount = getAccountById(userAccounts.get(0).getId()).getTransactions().size();

        new CrudRequest(
                RequestSpecs.getUserSpec(user1.getUsername(), user1.getPassword()),
                Endpoint.TRANSFER,
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
        BankWidget.dodepInAccount(user1.getUsername(), user1.getPassword(), userAccounts.get(0).getId(), balance);

        TransferRequestDTO transferRequestDTO = TransferRequestDTO.builder()
                .senderAccountId(userAccounts.get(0).getId())
                .receiverAccountId(userAccounts.get(receiverAccountId).getId())
                .amount(amount)
                .build();

        int startCount = getAccountById(userAccounts.get(0).getId()).getTransactions().size();

        new CrudRequest(
                RequestSpecs.getUserSpec(user1.getUsername(), user1.getPassword()),
                Endpoint.TRANSFER,
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
        BankWidget.dodepInAccount(user1.getUsername(), user1.getPassword(), userAccounts.get(0).getId(), balance);

        TransferRequestDTO transferRequestDTO = TransferRequestDTO.builder()
                .senderAccountId(userAccounts.get(0).getId())
                .receiverAccountId(0)
                .amount(1500.0)
                .build();

        int startCount = getAccountById(userAccounts.get(0).getId()).getTransactions().size();

        new CrudRequest(
                RequestSpecs.getUserSpec(user1.getUsername(), user1.getPassword()),
                Endpoint.TRANSFER,
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
