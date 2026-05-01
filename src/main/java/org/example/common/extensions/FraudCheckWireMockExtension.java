package org.example.common.extensions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import org.example.common.annotations.FraudCheckMock;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class FraudCheckWireMockExtension implements BeforeEachCallback, AfterEachCallback {
    private WireMockServer wireMockServer;

    @Override
    public void beforeEach(ExtensionContext context) {
        // Find the FraudCheckMock annotation on the test method or class
        FraudCheckMock mockConfig = context.getTestMethod()
                .map(method -> method.getAnnotation(FraudCheckMock.class))
                .orElseGet(() -> context.getTestClass()
                        .map(clazz -> clazz.getAnnotation(FraudCheckMock.class))
                        .orElse(null));

        if (mockConfig != null) {
            setupWireMock(mockConfig);
        }
    }

    private void setupWireMock(FraudCheckMock config) {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(config.port()));
        wireMockServer.start();
        WireMock.configureFor("0.0.0.0", config.port());

        // Create the response body based on annotation parameters
        /*String responseBody = String.format("{\n" +
                        "  \"status\": \"%s\",\n" +
                        "  \"decision\": \"%s\",\n" +
                        "  \"riskScore\": %.1f,\n" +
                        "  \"reason\": \"%s\",\n" +
                        "  \"requiresManualReview\": %s,\n" +
                        "  \"additionalVerificationRequired\": %s\n" +
                        "}",
                config.status(),
                config.decision(),
                config.riskScore(),
                config.reason(),
                config.requiresManualReview(),
                config.additionalVerificationRequired());


         */
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", config.status());
        response.put("decision", config.decision());
        response.put("riskScore", config.riskScore());
        response.put("reason", config.reason());
        response.put("requiresManualReview", config.requiresManualReview());
        response.put("additionalVerificationRequired", config.additionalVerificationRequired());

        String responseBody = null;
        try {
            responseBody = new ObjectMapper().writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        // Mock the fraud detection service endpoint
        stubFor(post(urlPathMatching(config.endpoint()))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)));
    }

    @Override
    public void afterEach(ExtensionContext context) {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    public String getBaseUrl() {
        if (wireMockServer != null) {
            return "http://host.docker.internal:" + wireMockServer.port();
        }
        return null;
    }

    public static class FraudCheckResponseMock {
        public String status;
        public String decision;
        public double riskScore;
        public String reason;
        public boolean requiresManualReview;
        public boolean additionalVerificationRequired;
    }
}
