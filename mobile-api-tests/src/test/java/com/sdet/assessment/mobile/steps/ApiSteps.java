package com.sdet.assessment.mobile.steps;

import com.sdet.assessment.mobile.api.UserClient;
import com.sdet.assessment.mobile.config.ConfigReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class ApiSteps {

    private final UserClient userClient = new UserClient();

    private Response lastResponse;
    private Map<String, String> requestBody;

    @Given("the ReqRes API is available")
    public void theReqResApiIsAvailable() {
        // Base URI comes from configuration; nothing to do here.
    }

    @When("I request page {int} of users")
    public void iRequestPageOfUsers(int page) {
        lastResponse = userClient.getUsers(page);
    }

    @Then("the response status is {int}")
    public void theResponseStatusIs(int expectedStatus) {
        Assert.assertEquals(lastResponse.statusCode(), expectedStatus, "Unexpected HTTP status");
    }

    @Then("the first name of user with id {int} is {string}")
    public void theFirstNameOfUserWithIdIs(int id, String expectedName) {
        String actual = first_nameOf(id);
        Assert.assertEquals(actual, expectedName, "Unexpected first_name for id " + id);
    }

    @When("I create a user from the first name of user with id {int} and the configured job")
    public void iCreateAUserFromUserWithId(int id) {
        requestBody = new LinkedHashMap<>();
        requestBody.put("name", first_nameOf(id));
        requestBody.put("job", ConfigReader.get("api.job"));
        lastResponse = userClient.createUser(requestBody);
    }

    @Then("the generated id is not empty")
    public void theGeneratedIdIsNotEmpty() {
        String id = lastResponse.jsonPath().getString("id");
        Assert.assertNotNull(id, "Generated id should not be null");
        Assert.assertFalse(id.trim().isEmpty(), "Generated id should not be empty");
    }

    @Then("the response matches the JSON schema {string}")
    public void theResponseMatchesTheJsonSchema(String schemaPath) {
        lastResponse.then().assertThat().body(matchesJsonSchemaInClasspath(schemaPath));
    }

    private String first_nameOf(int id) {
        return lastResponse.jsonPath().getString("data.find { it.id == " + id + " }.first_name");
    }
}
