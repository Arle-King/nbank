package junior_api_autotest;

import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;

public class BankWidget extends BankRequest {

    public static String createUserAndGetToken() {
        JsonObject userJson = new JsonObject();
        userJson.addProperty("username", RandomStringUtils.randomAlphanumeric(6));
        userJson.addProperty("password","Password#1");
        userJson.addProperty ("role" , "USER");
        return RestAssured.given()
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter())
                .filter(new ErrorLoggingFilter())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", "Basic YWRtaW46YWRtaW4=")
                .body(userJson)
                .post("http://localhost:4111/api/v1/admin/users")
                .then()
                .statusCode(201)
                .extract()
                .header("Authorization");
    }

    public static int createAndGetIdAccount(String token) {
        return RestAssured.given()
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter())
                .filter(new ErrorLoggingFilter())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", token)
                .post("http://localhost:4111/api/v1/accounts")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }

    public static void dodepInAccount(String token, int accountId, int amount) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", accountId);

        do {
            if (amount > 5001) {
                jsonObject.addProperty("balance", 5000);
            } else {
                jsonObject.addProperty("balance", amount);
            }

            RestAssured.given()
                    .filter(new RequestLoggingFilter())
                    .filter(new ResponseLoggingFilter())
                    .filter(new ErrorLoggingFilter())
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .header("Authorization", token)
                    .body(jsonObject)
                    .post("http://localhost:4111/api/v1/accounts/deposit");

            amount = amount - 5000;
        } while (amount > 0);
    }
}
