package org.example.specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;

public class ResponceSpecs {

    private static ResponseSpecBuilder defaultResponseBuilder() {
        return new ResponseSpecBuilder();
    }

    public static ResponseSpecification entityWasCreated() {
        return defaultResponseBuilder()
                .expectStatusCode(HttpStatus.SC_CREATED)
                .build();
    }

    public static ResponseSpecification requestReturnsOk() {
        return defaultResponseBuilder()
                .expectStatusCode(HttpStatus.SC_OK)
                .build();
    }
}
