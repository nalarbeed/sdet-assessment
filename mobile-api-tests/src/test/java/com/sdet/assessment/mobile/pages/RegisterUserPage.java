package com.sdet.assessment.mobile.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.LinkedHashMap;
import java.util.Map;

public class RegisterUserPage extends BasePage {

    private static final String PKG = "io.selendroid.testapp";

    private static final By TITLE = By.id("android:id/title");
    private static final By HEADING = By.xpath("//android.widget.TextView[@text='Welcome to register a new User']");

    private static final String USERNAME = PKG + ":id/inputUsername";
    private static final String EMAIL = PKG + ":id/inputEmail";
    private static final String PASSWORD = PKG + ":id/inputPassword";
    private static final String NAME = PKG + ":id/inputName";
    private static final String LANGUAGE_SPINNER = PKG + ":id/input_preferedProgrammingLanguage";
    private static final String ACCEPT_ADDS = PKG + ":id/input_adds";
    private static final String REGISTER = PKG + ":id/btnRegisterUser";

    private static final Map<String, String> ELEMENT_IDS = new LinkedHashMap<>();

    static {
        ELEMENT_IDS.put("Username", USERNAME);
        ELEMENT_IDS.put("E-Mail", EMAIL);
        ELEMENT_IDS.put("Password", PASSWORD);
        ELEMENT_IDS.put("Name", NAME);
        ELEMENT_IDS.put("Programming Language", LANGUAGE_SPINNER);
        ELEMENT_IDS.put("I accept adds", ACCEPT_ADDS);
        ELEMENT_IDS.put("Register User", REGISTER);
    }

    public RegisterUserPage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return isDisplayed(HEADING);
    }

    public String title() {
        return getText(TITLE);
    }

    public boolean showsTextStartingWith(String prefix) {
        return getText(HEADING).startsWith(prefix);
    }

    public boolean isElementDisplayed(String element) {
        String id = requireId(element);
        scrollTo(id);
        return isDisplayed(By.id(id), 3);
    }

    public String name() {
        return getText(By.id(NAME));
    }

    public String language() {
        scrollTo(LANGUAGE_SPINNER);
        return driver.findElement(By.id(LANGUAGE_SPINNER))
                .findElement(By.id("android:id/text1"))
                .getText();
    }

    public void fillField(String field, String value) {
        String id = requireId(field);
        scrollTo(id);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
        element.clear();
        element.sendKeys(value);
    }

    public void selectLanguage(String language) {
        scrollTo(LANGUAGE_SPINNER);
        click(By.id(LANGUAGE_SPINNER));
        click(By.xpath("//android.widget.CheckedTextView[@text='" + language + "']"));
    }

    public void acceptAdds() {
        scrollTo(ACCEPT_ADDS);
        click(By.id(ACCEPT_ADDS));
    }

    public void register() {
        scrollTo(REGISTER);
        click(By.id(REGISTER));
    }

    private String requireId(String element) {
        String id = ELEMENT_IDS.get(element);
        if (id == null) {
            throw new IllegalArgumentException("Unknown registration element: " + element);
        }
        return id;
    }

    private void scrollTo(String resourceId) {
        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView("
                        + "new UiSelector().resourceId(\"" + resourceId + "\"))"));
    }
}
