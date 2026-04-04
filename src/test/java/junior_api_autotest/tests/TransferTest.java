package junior_api_autotest.tests;

import com.google.gson.JsonObject;
import io.restassured.http.ContentType;
import junior_api_autotest.BankRequest;
import junior_api_autotest.BankWidget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TransferTest extends BankRequest {

    private static String versionApi = "/v1";
    public static String subService = "/accounts";
    public static String endPoint = "/transfer";

    JsonObject transfer;

    String firstUserToken;
    List<Integer> accountId;

    String secondUserOneToken;


    @BeforeEach
    public void precondition() {
        transfer = new JsonObject();
        accountId = new ArrayList<>();

        firstUserToken = BankWidget.createUserAndGetToken();
        accountId.add(BankWidget.createAndGetIdAccount(firstUserToken));
        accountId.add(BankWidget.createAndGetIdAccount(firstUserToken));

        secondUserOneToken = BankWidget.createUserAndGetToken();
        accountId.add(BankWidget.createAndGetIdAccount(secondUserOneToken));
        accountId.add(0);
    }

    @ParameterizedTest
    @MethodSource("provaderTransfer")
    public void positivTest(int balance, int receiverAccountId, int amount, int statusCode) {
        BankWidget.dodepInAccount(firstUserToken, accountId.get(0), balance);

        transfer.addProperty("senderAccountId", accountId.get(0));
        transfer.addProperty("receiverAccountId", accountId.get(receiverAccountId));
        transfer.addProperty("amount", amount);

        spec.contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", firstUserToken)
                .body(transfer)
                .post(baseUrl + versionApi + subService + endPoint)
                .then()
                .assertThat()
                .statusCode(statusCode);

    }

    // 0 - основной счёт, 1 - доп счёт, 2 - чужой счёт, 3 - несуществующий счёт
    public static Stream<Arguments> provaderTransfer() {
        return Stream.of(
                Arguments.of( 10100, 1, 10000, 200), // макс перевод себе
                Arguments.of( 10100, 2, 10000, 200), // макс перевод на чужой счёт
                Arguments.of( 100, 1, 0, 400), // перевод 0 денег
                Arguments.of( 1000, 1, -100, 400), // перевод отрицательного кол-ва денег
                Arguments.of( 10100, 1, 10050, 400), // выход за пределы допустимого перевода
                Arguments.of( 4000, 1, 4500, 400), // перевод без необходимой суммы на счету
                Arguments.of( 4000, 3, 3500, 400) // // перевод на несуществующий счёт
        );
    }
}
