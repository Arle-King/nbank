package org.example.accounts.requests.transfer;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.example.Request;
import org.example.accounts.models.transfer.TransferRequestDTO;
import org.example.interfaces.Postble;

import static io.restassured.RestAssured.given;

public class TransferRequest extends Request implements Postble<TransferRequestDTO> {
    public TransferRequest(RequestSpecification spec, ResponseSpecification resp) {
        super(spec, resp);
    }

    @Override
    public ValidatableResponse post(TransferRequestDTO model) {
        return given()
                .spec(requestSpecification)
                .body(model)
                .post("api/v1/accounts/transfer")
                .then()
                .assertThat()
                .spec(responseSpecification);
    }
}
