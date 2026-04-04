package junior_api_autotest.tests;

import com.google.gson.JsonObject;
import io.restassured.http.ContentType;
import junior_api_autotest.BankRequest;
import junior_api_autotest.BankWidget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class DepositTest extends BankRequest {

    private static String versionApi = "/v1";
    private static String subService = "/accounts";
    private static String endPoint = "/deposit";

    JsonObject deposit;
    String userToken;
    int accountId;


    @BeforeEach
    public void precondition() {
        deposit = new JsonObject();
        userToken = BankWidget.createUserAndGetToken();
        accountId = BankWidget.createAndGetIdAccount(userToken);
    }

    @ParameterizedTest
    @MethodSource("provaderDeposit")
    public void positivTest(int balance, int statusCode) {
        deposit.addProperty("id", accountId);
        deposit.addProperty("balance", balance);

        spec.contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", userToken)
                .body(deposit)
                .post(baseUrl + versionApi + subService + endPoint)
                .then()
                .assertThat()
                .statusCode(statusCode);
    }

    @Test
    public void addAlienAccountTest() {
        String userToken2 = BankWidget.createUserAndGetToken();
        int accountId2 = BankWidget.createAndGetIdAccount(userToken2);

        deposit.addProperty("id", accountId2);
        deposit.addProperty("balance", 100);

        spec.contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", userToken)
                .body(deposit)
                .post(baseUrl + versionApi + subService + endPoint)
                .then()
                .assertThat()
                .statusCode(403);
    }

    @Test
    public void addNonExistentAccountTest() {

        deposit.addProperty("id", accountId - accountId);
        deposit.addProperty("balance", 100);

        spec.contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", userToken)
                .body(deposit)
                .post(baseUrl + versionApi + subService + endPoint)
                .then()
                .assertThat()
                .statusCode(403);
    }

    public static Stream<Arguments> provaderDeposit() {
        return Stream.of(
                Arguments.of( 100, 200),
                Arguments.of( 5000, 200),
                Arguments.of( 5001, 403),
                Arguments.of(0, 400),
                Arguments.of(-1, 400)
        );
    }
}
