package org.example.specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.authentication.models.LoginRequestDTO;
import org.example.authentication.requests.LoginRequest;

import java.util.List;

public class RequestSpecs {

    private static RequestSpecBuilder defaultRequestSpecification() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter(), new ErrorLoggingFilter()))
                .setBaseUri("http://localhost:4111");
    }

    public static RequestSpecification getUnAuthSpec() {
        return defaultRequestSpecification().build();
    }

    public static RequestSpecification getAdminSpec() {
        return defaultRequestSpecification()
                .addHeader("Authorization", "Basic YWRtaW46YWRtaW4=")
                .build();
    }

    public static RequestSpecification getUserSpec(String userName, String password) {
        String token = new LoginRequest(
                RequestSpecs.getUnAuthSpec(),
                ResponceSpecs.requestReturnsOk())
                .post(LoginRequestDTO.builder().userName(userName).password(password).build())
                .extract()
                .header("Authorization");

        return defaultRequestSpecification()
                .addHeader("Authorization", token)
                .build();
    }

}
