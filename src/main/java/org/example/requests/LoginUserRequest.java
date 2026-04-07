package org.example.requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.example.models.users.LoginUserRequestDTO;

import static io.restassured.RestAssured.given;

public class LoginUserRequest extends Request<LoginUserRequestDTO>{
    public LoginUserRequest(RequestSpecification spec, ResponseSpecification resp) {
        super(spec, resp);
    }

    @Override
    public ValidatableResponse post(LoginUserRequestDTO model) {
        return given()
                .spec(requestSpecification)
                .body(model)
                .post("api/v1/auth/login")
                .then()
                .assertThat()
                .spec(responseSpecification);
    }
}
