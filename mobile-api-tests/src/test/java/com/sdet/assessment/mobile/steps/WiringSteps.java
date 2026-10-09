package com.sdet.assessment.mobile.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.testng.Assert;

public class WiringSteps {

    private int result;

    @Given("I add {int} and {int}")
    public void iAdd(int first, int second) {
        this.result = first + second;
    }

    @Then("the result is {int}")
    public void theResultIs(int expected) {
        Assert.assertEquals(this.result, expected, "Wiring assertion failed");
    }
}
