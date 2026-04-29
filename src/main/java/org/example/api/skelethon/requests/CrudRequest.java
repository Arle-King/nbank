package org.example.api.skelethon.requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.example.api.models.BaseModel;
import org.example.api.skelethon.enams.Endpoint;
import org.example.api.skelethon.interfaces.CrudEndpointInterface;

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
    public ValidatableResponse get(int... id) {
        String url = endpoint.getEndpoint();
        for (int i = 1; i < id.length + 1; i++) {
            url = url.replace("id#" + i, String.valueOf(id[i - 1]));
        }
        return given()
                .spec(requestSpecification)
                .get(url)
                .then()
                .assertThat()
                .spec(responseSpecification);
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
    public ValidatableResponse update(BaseModel model, int... id) {
        String url = endpoint.getEndpoint();
        for (int i = 1; i < id.length + 1; i++) {
            url = url.replace("id#" + i, String.valueOf(id[i - 1]));
        }
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
    public ValidatableResponse update(BaseModel model) {
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
    public ValidatableResponse delete(int... id) {
        String url = endpoint.getEndpoint();
        for (int i = 1; i < id.length + 1; i++) {
            url = url.replace("id#" + i, String.valueOf(id[i - 1]));
        }
        return given()
                .spec(requestSpecification)
                .delete(url)
                .then()
                .assertThat()
                .spec(responseSpecification);
    }
}
