package com.sdet.assessment.mobile.steps;

import com.sdet.assessment.mobile.driver.AppiumDriverManager;
import com.sdet.assessment.mobile.pages.HomePage;
import com.sdet.assessment.mobile.pages.LocalizationPage;
import com.sdet.assessment.mobile.pages.PopupWindowPage;
import com.sdet.assessment.mobile.pages.ToastPage;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class MobileSteps {

    private static final Logger log = LoggerFactory.getLogger(MobileSteps.class);

    private HomePage homePage;
    private LocalizationPage localizationPage;
    private PopupWindowPage popupWindowPage;

    private AndroidDriver driver() {
        return AppiumDriverManager.get();
    }

    private HomePage home() {
        if (homePage == null) {
            homePage = new HomePage(driver());
        }
        return homePage;
    }

    @Given("I am on the home screen")
    public void iAmOnTheHomeScreen() {
        Assert.assertTrue(home().isLoaded(), "Home screen is not displayed");
    }

    @Then("the title is {string}")
    public void theTitleIs(String expected) {
        Assert.assertEquals(home().title(), expected, "Unexpected screen title");
    }

    @Then("the home screen shows the elements")
    public void theHomeScreenShowsTheElements(DataTable table) {
        for (String element : table.asList()) {
            Assert.assertTrue(home().isElementDisplayed(element), "Element not displayed: " + element);
        }
    }

    @When("I tap the {string} button")
    public void iTapTheButton(String button) {
        log.info("Tapping '{}'", button);
        home().tap(button);
    }

    @Then("the localization dialog shows message {string}")
    public void theLocalizationDialogShowsMessage(String expected) {
        localizationPage = new LocalizationPage(driver());
        Assert.assertTrue(localizationPage.isDisplayed(), "Localization dialog not displayed");
        Assert.assertEquals(localizationPage.message(), expected, "Unexpected dialog message");
    }

    @When("I choose {string}")
    public void iChoose(String option) {
        log.info("Choosing '{}'", option);
        localizationPage.choose(option);
    }

    @Then("the home screen is displayed")
    public void theHomeScreenIsDisplayed() {
        Assert.assertTrue(home().isLoaded(), "Home screen is not displayed again");
    }

    @Then("the toast text is {string}")
    public void theToastTextIs(String expected) {
        String actual = new ToastPage(driver()).waitForText();
        log.info("Toast text: {}", actual);
        Assert.assertEquals(actual, expected, "Unexpected toast text");
    }

    @Then("the popup window is displayed")
    public void thePopupWindowIsDisplayed() {
        popupWindowPage = new PopupWindowPage(driver());
        Assert.assertTrue(popupWindowPage.isDisplayed(), "Popup window not displayed");
    }

    @When("I dismiss the popup window")
    public void iDismissThePopupWindow() {
        log.info("Dismissing popup window");
        popupWindowPage.dismiss();
    }

    @Then("the popup window is gone")
    public void thePopupWindowIsGone() {
        Assert.assertTrue(popupWindowPage.isDismissed(), "Popup window is still displayed");
    }
}
