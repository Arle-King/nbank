package org.example.customer.requests.profile;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.example.BaseModel;
import org.example.Request;
import org.example.interfaces.Updatble;

import static io.restassured.RestAssured.given;

public class UpdateProfileRequest extends Request implements Updatble {
    public UpdateProfileRequest(RequestSpecification spec, ResponseSpecification resp) {
        super(spec, resp);
    }

    @Override
    public ValidatableResponse update(BaseModel model) {
        return given()
                .spec(requestSpecification)
                .body(model)
                .put("/api/v1/customer/profile")
                .then()
                .assertThat()
                .spec(responseSpecification);
    }
}
