package org.example.accounts.requests.deposit;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.example.Request;
import org.example.accounts.models.deposit.DepositRequestDTO;
import org.example.interfaces.Postble;

import static io.restassured.RestAssured.given;

public class DepositRequest extends Request implements Postble<DepositRequestDTO> {
    public DepositRequest(RequestSpecification spec, ResponseSpecification resp) {
        super(spec, resp);
    }

    @Override
    public ValidatableResponse post(DepositRequestDTO model) {
        return given()
                .spec(requestSpecification)
                .body(model)
                .post("api/v1/accounts/deposit")
                .then()
                .assertThat()
                .spec(responseSpecification);
    }
}
