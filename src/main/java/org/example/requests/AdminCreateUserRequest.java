package org.example.requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.example.models.BaseModel;
import org.example.models.users.CreateUserRequestDTO;

import static io.restassured.RestAssured.given;

public class AdminCreateUserRequest extends Request<CreateUserRequestDTO> {

    public AdminCreateUserRequest(RequestSpecification spec, ResponseSpecification resp) {
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
