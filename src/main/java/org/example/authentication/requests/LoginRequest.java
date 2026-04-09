package org.example.authentication.requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.example.authentication.models.LoginRequestDTO;
import org.example.Request;
import org.example.interfaces.Postble;

import static io.restassured.RestAssured.given;

public class  LoginRequest extends Request implements Postble<LoginRequestDTO> {
    public LoginRequest(RequestSpecification spec, ResponseSpecification resp) {
        super(spec, resp);
    }

    @Override
    public ValidatableResponse post(LoginRequestDTO model) {
        return given()
                .spec(requestSpecification)
                .body(model)
                .post("api/v1/auth/login")
                .then()
                .assertThat()
                .spec(responseSpecification);
    }
}
