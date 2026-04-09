package org.example;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public abstract class Request {

    protected Response resp;

    protected RequestSpecification requestSpecification;
    protected ResponseSpecification responseSpecification;

    public Request(RequestSpecification spec, ResponseSpecification resp) {
        this.requestSpecification = spec;
        this.responseSpecification = resp;
    }
}
