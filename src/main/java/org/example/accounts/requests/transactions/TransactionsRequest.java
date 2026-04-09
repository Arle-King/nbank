package org.example.accounts.requests.transactions;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.example.Request;
import org.example.interfaces.Getble;

import static io.restassured.RestAssured.given;

public class TransactionsRequest extends Request implements Getble {

    public TransactionsRequest(RequestSpecification spec, ResponseSpecification resp) {
        super(spec, resp);
    }

    @Override
    public ValidatableResponse get(int id) {
        return given()
                .spec(requestSpecification)
                .post("api/v1/accounts/" + id + "/transactions")
                .then()
                .assertThat()
                .spec(responseSpecification);
    }
}
