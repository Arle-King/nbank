package junior_api_autotest;

import io.restassured.RestAssured;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

public class BankRequest {

    public RequestSpecification spec = RestAssured.given()
            .filter(new RequestLoggingFilter())
            .filter(new ResponseLoggingFilter())
            .filter(new ErrorLoggingFilter());

    public static String baseUrl = "http://localhost:4111/api";

}
