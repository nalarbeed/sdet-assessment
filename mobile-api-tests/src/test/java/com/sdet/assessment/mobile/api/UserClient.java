package com.sdet.assessment.mobile.api;

import com.sdet.assessment.mobile.config.ConfigReader;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserClient {

    private static final Logger log = LoggerFactory.getLogger(UserClient.class);

    private final String baseUri;

    public UserClient() {
        this.baseUri = ConfigReader.get("api.base.uri");
    }

    public Response getUsers(int page) {
        String uri = "/api/users?page=" + page;
        Response response = request()
                .queryParam("page", page)
                .when()
                .get("/api/users")
                .then()
                .extract().response();
        log.info("GET {} -> status {}", uri, response.statusCode());
        return response;
    }

    public Response createUser(Map<String, String> body) {
        String uri = "/api/users";
        Response response = request()
                .body(body)
                .when()
                .post(uri)
                .then()
                .extract().response();
        log.info("POST {} -> status {}", uri, response.statusCode());
        return response;
    }

    private RequestSpecification request() {
        RequestSpecification spec = given()
                .baseUri(baseUri)
                .contentType("application/json");

        String apiKey = System.getenv("REQRES_API_KEY");
        if (apiKey != null && !apiKey.trim().isEmpty()) {
            spec = spec.header("x-api-key", apiKey);
        }
        return spec;
    }
}
