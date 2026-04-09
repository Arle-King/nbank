package org.example.admin.requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.example.Request;
import org.example.admin.models.CreateUserRequestDTO;
import org.example.interfaces.Postble;

import static io.restassured.RestAssured.given;

public class CreateUserRequest extends Request implements Postble<CreateUserRequestDTO> {

    public CreateUserRequest(RequestSpecification spec, ResponseSpecification resp) {
        super(spec, resp);
    }

    @Override
    public ValidatableResponse post(CreateUserRequestDTO model) {
        return given()
                .spec(requestSpecification)
                .body(model)
                .post("/api/v1/admin/users")
                .then()
                .assertThat()
                .spec(responseSpecification);
    }
}
