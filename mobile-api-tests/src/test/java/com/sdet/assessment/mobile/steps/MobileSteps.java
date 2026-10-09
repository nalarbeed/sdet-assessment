package com.sdet.assessment.mobile.steps;

import com.sdet.assessment.mobile.driver.AppiumDriverManager;
import com.sdet.assessment.mobile.pages.HomePage;
import com.sdet.assessment.mobile.pages.LocalizationPage;
import com.sdet.assessment.mobile.pages.PopupWindowPage;
import com.sdet.assessment.mobile.pages.RegisterUserPage;
import com.sdet.assessment.mobile.pages.ToastPage;
import com.sdet.assessment.mobile.pages.VerifyUserPage;
import com.sdet.assessment.mobile.pages.WebViewPage;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.util.Map;

public class MobileSteps {

    private static final Logger log = LoggerFactory.getLogger(MobileSteps.class);

    private HomePage homePage;
    private LocalizationPage localizationPage;
    private PopupWindowPage popupWindowPage;
    private RegisterUserPage registerUserPage;
    private VerifyUserPage verifyUserPage;
    private WebViewPage webViewPage;

    private AndroidDriver driver() {
        return AppiumDriverManager.get();
    }

    private HomePage home() {
        if (homePage == null) {
            homePage = new HomePage(driver());
        }
        return homePage;
    }

    private RegisterUserPage register() {
        if (registerUserPage == null) {
            registerUserPage = new RegisterUserPage(driver());
        }
        return registerUserPage;
    }

    private VerifyUserPage verify() {
        if (verifyUserPage == null) {
            verifyUserPage = new VerifyUserPage(driver());
        }
        return verifyUserPage;
    }

    private WebViewPage webView() {
        if (webViewPage == null) {
            webViewPage = new WebViewPage(driver());
        }
        return webViewPage;
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

    @When("I tap the file logo button")
    public void iTapTheFileLogoButton() {
        log.info("Tapping the file logo button");
        home().tapFileLogo();
    }

    @When("I wait for the progress loader to disappear")
    public void iWaitForTheProgressLoaderToDisappear() {
        log.info("Waiting for the progress loader to disappear");
        home().waitForProgressLoaderToDisappear();
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

    @Then("the registration screen title is {string}")
    public void theRegistrationScreenTitleIs(String expected) {
        Assert.assertEquals(register().title(), expected, "Unexpected registration screen title");
    }

    @Then("the registration screen shows text starting with {string}")
    public void theRegistrationScreenShowsTextStartingWith(String prefix) {
        Assert.assertTrue(register().showsTextStartingWith(prefix),
                "Registration screen text does not start with: " + prefix);
    }

    @Then("the registration screen shows these elements")
    public void theRegistrationScreenShowsTheseElements(DataTable table) {
        for (String element : table.asList()) {
            Assert.assertTrue(register().isElementDisplayed(element),
                    "Registration element not displayed: " + element);
        }
    }

    @Then("the Name field is {string}")
    public void theNameFieldIs(String expected) {
        Assert.assertEquals(register().name(), expected, "Unexpected Name value");
    }

    @Then("the default Programming Language is {string}")
    public void theDefaultProgrammingLanguageIs(String expected) {
        Assert.assertEquals(register().language(), expected, "Unexpected default programming language");
    }

    @When("I fill the registration form with:")
    public void iFillTheRegistrationFormWith(DataTable table) {
        for (Map<String, String> row : table.asMaps(String.class, String.class)) {
            String field = row.get("Field");
            String value = row.get("Value");
            log.info("Filling registration field '{}'", field);
            register().fillField(field, value);
        }
    }

    @When("I select the Programming Language {string}")
    public void iSelectTheProgrammingLanguage(String language) {
        log.info("Selecting programming language '{}'", language);
        register().selectLanguage(language);
    }

    @When("I accept adds")
    public void iAcceptAdds() {
        log.info("Accepting adds on the registration screen");
        register().acceptAdds();
    }

    @When("I tap Register User")
    public void iTapRegisterUser() {
        log.info("Tapping Register User (registration screen)");
        register().register();
    }

    @Then("the verify screen shows the registered user with:")
    public void theVerifyScreenShowsTheRegisteredUserWith(DataTable table) {
        Assert.assertTrue(verify().isDisplayed(), "Verify user screen is not displayed");
        for (Map<String, String> row : table.asMaps(String.class, String.class)) {
            String field = row.get("Field");
            String expected = row.get("Value");
            Assert.assertEquals(verify().valueOf(field), expected,
                    "Unexpected value for '" + field + "'");
        }
    }

    @When("I tap Register User again")
    public void iTapRegisterUserAgain() {
        log.info("Tapping Register User (verify screen)");
        verify().register();
    }

    @When("I type {string} in the exception field")
    public void iTypeInTheExceptionField(String text) {
        log.info("Typing '{}' in the type-to-throw-unhandled-exception field", text);
        home().typeIntoExceptionField(text);
    }

    @Then("the home screen title is displayed")
    public void theHomeScreenTitleIsDisplayed() {
        Assert.assertTrue(home().isLoaded(), "Home screen is not displayed (the app likely crashed)");
        Assert.assertEquals(home().title(), "selendroid-test-app", "Unexpected home screen title");
    }

    @When("I tap the Chrome logo button")
    public void iTapTheChromeLogoButton() {
        log.info("Tapping the Chrome logo button");
        home().tapChromeLogo();
    }

    @When("I switch to the web view")
    public void iSwitchToTheWebView() {
        log.info("Switching to the web view context");
        webView().switchToWebView();
    }

    @When("I switch back to the native view")
    public void iSwitchBackToTheNativeView() {
        log.info("Switching back to the native context");
        webView().switchToNative();
    }

    @Then("the web view title is {string}")
    public void theWebViewTitleIs(String expected) {
        Assert.assertEquals(webView().title(), expected, "Unexpected web view title");
    }

    @Then("the web view shows text starting with {string}")
    public void theWebViewShowsTextStartingWith(String prefix) {
        String text = webView().bodyText();
        Assert.assertTrue(text.startsWith(prefix),
                "Web view text does not start with: " + prefix + " (was: " + text + ")");
    }

    @When("I enter the name {string} in the web view")
    public void iEnterTheNameInTheWebView(String name) {
        log.info("Entering name '{}' in the web view", name);
        webView().enterName(name);
    }

    @When("I select the Preferred Car {string}")
    public void iSelectThePreferredCar(String car) {
        log.info("Selecting preferred car '{}'", car);
        webView().selectCar(car);
    }

    @When("I tap {string} in the web view")
    public void iTapInTheWebView(String button) {
        log.info("Submitting the web view form ('{}')", button);
        webView().submit();
    }

    @Then("the web view shows the name {string}")
    public void theWebViewShowsTheName(String name) {
        Assert.assertTrue(webView().bodyText().contains(name),
                "Web view does not show the name: " + name);
    }

    @Then("the web view shows the preferred car {string}")
    public void theWebViewShowsThePreferredCar(String car) {
        Assert.assertTrue(webView().bodyText().toLowerCase().contains(car.toLowerCase()),
                "Web view does not show the preferred car: " + car);
    }

    @When("I click the {string} link")
    public void iClickTheLink(String linkText) {
        log.info("Clicking the '{}' link in the web view", linkText);
        webView().clickLink(linkText);
    }

    @Then("the default Preferred Car is {string}")
    public void theDefaultPreferredCarIs(String expected) {
        Assert.assertEquals(webView().defaultCar(), expected, "Unexpected default preferred car");
    }
}
