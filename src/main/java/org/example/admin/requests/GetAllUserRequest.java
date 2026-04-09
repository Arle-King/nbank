package org.example.admin.requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.example.Request;

import static io.restassured.RestAssured.given;

public class GetAllUserRequest extends Request {
    public GetAllUserRequest(RequestSpecification spec, ResponseSpecification resp) {
        super(spec, resp);
    }

    public ValidatableResponse getAllUsers() {
        return given()
                .spec(requestSpecification)
                .get("/api/v1/admin/users")
                .then()
                .assertThat()
                .spec(responseSpecification);
    }
}
