package org.example.accounts.requests.accounts;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.example.Request;
import org.example.accounts.models.accounts.CreateUserAccountRequestDTO;
import org.example.interfaces.Postble;

import static io.restassured.RestAssured.given;

public class CreateUserAccountRequest extends Request implements Postble<CreateUserAccountRequestDTO> {
    public CreateUserAccountRequest(RequestSpecification spec, ResponseSpecification resp) {
        super(spec, resp);
    }

    @Override
    public ValidatableResponse post(CreateUserAccountRequestDTO model) {
        return given()
                .spec(requestSpecification)
                .body(model)
                .post("api/v1/accounts")
                .then()
                .assertThat()
                .spec(responseSpecification);
    }
}
