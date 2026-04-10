package org.example.specs;

import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.models.authentication.login.LoginRequestDTO;
import org.example.requests.skelethon.requests.CrudRequest;
import org.example.requests.skelethon.enams.Endpoint;

import java.util.List;

public class RequestSpecs {

    private static io.restassured.builder.RequestSpecBuilder defaultRequestSpecification() {
        return new io.restassured.builder.RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter(), new ErrorLoggingFilter()))
                .setBaseUri("http://localhost:4111/api/v1");
    }

    public static RequestSpecification getUnAuthSpec() {
        return defaultRequestSpecification().build();
    }

    public static RequestSpecification getAdminSpec() {
        return defaultRequestSpecification()
                .addHeader("Authorization", "Basic YWRtaW46YWRtaW4=")
                .build();
    }


    public static RequestSpecification getUserSpec(String username, String password) {
        String token = new CrudRequest(
                RequestSpecs.getUnAuthSpec(),
                Endpoint.LOGIN,
                ResponceSpecs.requestReturnsOk())
                .post(LoginRequestDTO.builder().username(username).password(password).build())
                .extract()
                .header("Authorization");

        return defaultRequestSpecification()
                .addHeader("Authorization", token)
                .build();
    }


}
