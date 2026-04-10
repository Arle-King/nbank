package org.example.requests.skelethon.requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.example.models.BaseModel;
import org.example.requests.skelethon.enams.Endpoint;
import org.example.requests.skelethon.interfaces.CrudEndpointInterface;

import static io.restassured.RestAssured.given;

public class CrudRequest extends HttpRequest implements CrudEndpointInterface {
    public CrudRequest(RequestSpecification requestSpecification, Endpoint endpoint, ResponseSpecification responseSpecification) {
        super(requestSpecification, endpoint, responseSpecification);
    }

    @Override
    public ValidatableResponse post(BaseModel model) {
        var body = model == null ? "" : model;
        return given()
                .spec(requestSpecification)
                .body(body)
                .post(endpoint.getEndpoint())
                .then()
                .assertThat()
                .spec(responseSpecification);
    }

    @Override
    public Object get(int id) {
        return null;
    }


    @Override
    public ValidatableResponse get() {
        return given()
                .spec(requestSpecification)
                .get(endpoint.getEndpoint())
                .then()
                .assertThat()
                .spec(responseSpecification);
    }

    @Override
    public ValidatableResponse update(int id, BaseModel model) {
        return null;
    }

    @Override
    public Object update(BaseModel model) {
        var body = model == null ? "" : model;
        return given()
                .spec(requestSpecification)
                .body(body)
                .put(endpoint.getEndpoint())
                .then()
                .assertThat()
                .spec(responseSpecification);
    }

    @Override
    public Object delete(int id) {
        return null;
    }
}
