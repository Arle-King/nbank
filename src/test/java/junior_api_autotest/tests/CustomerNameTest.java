package junior_api_autotest.tests;

import com.google.gson.JsonObject;
import io.restassured.http.ContentType;
import junior_api_autotest.BankRequest;
import junior_api_autotest.BankWidget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class CustomerNameTest extends BankRequest {

    private static String versionApi = "/v1";
    public static String subService = "/customer";
    public static String endPoint = "/profile";

    JsonObject customName;
    String userToken;

    @BeforeEach
    public void precondition() {
        customName = new JsonObject();

        userToken = BankWidget.createUserAndGetToken();
    }

    @ParameterizedTest
    @MethodSource("provaderCutomerName")
    public void customerNameTest(String name, int statusCode) {

        customName.addProperty("name", name);

        spec.contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", userToken)
                .body(customName)
                .put(baseUrl + versionApi + subService + endPoint)
                .then()
                .assertThat()
                .statusCode(statusCode);

    }

    public static Stream<Arguments> provaderCutomerName() {
        return Stream.of(
                Arguments.of( "New Name", 200),
                Arguments.of( "   ", 422),
                Arguments.of( "", 422),
                Arguments.of("New", 422),
                Arguments.of("New New Name", 422),
                Arguments.of("New Name!", 422),
                Arguments.of("New Name123", 422)
                );
    }
}
