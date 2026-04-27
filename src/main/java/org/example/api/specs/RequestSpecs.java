package org.example.api.specs;

import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.api.configs.Config;
import org.example.api.models.authentication.login.LoginRequestDTO;
import org.example.api.skelethon.enams.Endpoint;
import org.example.api.skelethon.requests.CrudRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestSpecs {
    private static Map<String, String> token = new HashMap<>(Map.of("admin", "Basic YWRtaW46YWRtaW4="));

    private static io.restassured.builder.RequestSpecBuilder defaultRequestSpecification() {
        return new io.restassured.builder.RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter(), new ErrorLoggingFilter()))
                .setBaseUri(Config.getProperty("api.baseUrl") + Config.getProperty("version"));
    }

    public static RequestSpecification getUnAuthSpec() {
        return defaultRequestSpecification().build();
    }

    public static RequestSpecification getAdminSpec() {
        return defaultRequestSpecification()
                .addHeader(ResponceSpecs.AUTH_HEADER, "Basic YWRtaW46YWRtaW4=")
                .build();
    }

    public static RequestSpecification getTokenSpec(String token) {
        return defaultRequestSpecification()
                .addHeader(ResponceSpecs.AUTH_HEADER, token)
                .build();
    }

    public static RequestSpecification getUserSpec(String username, String password) {
        return defaultRequestSpecification()
                .addHeader(ResponceSpecs.AUTH_HEADER, getAuthUserToken(username, password))
                .build();
    }

    public static String getAuthUserToken(String username, String password) {
        String userToken;
        if (!token.containsKey(username)) {
            userToken = new CrudRequest(
                    RequestSpecs.getUnAuthSpec(),
                    Endpoint.LOGIN,
                    ResponceSpecs.requestReturnsOk())
                    .post(LoginRequestDTO.builder().username(username).password(password).build())
                    .extract()
                    .header(ResponceSpecs.AUTH_HEADER);
            token.put(username, userToken);
        } else {
            userToken = token.get(username);
        }

        return userToken;
    }
}
