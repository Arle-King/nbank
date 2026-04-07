package org.example.requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.example.models.BaseModel;

public abstract class Request<T extends BaseModel> {

    protected RequestSpecification requestSpecification;
    protected ResponseSpecification responseSpecification;

    public Request(RequestSpecification spec, ResponseSpecification resp) {
        this.requestSpecification = spec;
        this.responseSpecification = resp;
    }

    public abstract ValidatableResponse post(T model);
}
