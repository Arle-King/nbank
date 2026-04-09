package org.example.customer.requests.profile;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.example.Request;

import static io.restassured.RestAssured.given;

public class GetProfileRequest extends Request {
    public GetProfileRequest(RequestSpecification spec, ResponseSpecification resp) {
        super(spec, resp);
    }

    public ValidatableResponse get() {
        return given()
                .spec(requestSpecification)
                .get("/api/v1/customer/profile")
                .then()
                .assertThat()
                .spec(responseSpecification);
    }
}
