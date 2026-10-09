package com.sdet.assessment.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

public class HomePage extends BasePage {

    private static final String PKG = "io.selendroid.testapp";

    private static final By TITLE = By.id("android:id/title");
    private static final By EN_BUTTON = By.id(PKG + ":id/buttonTest");
    private static final By START_WEBVIEW = By.id(PKG + ":id/buttonStartWebview");
    private static final By START_REGISTRATION = By.id(PKG + ":id/startUserRegistration");
    private static final By WAITING_BUTTON = By.id(PKG + ":id/waitingButtonTest");
    private static final By PROGRESS = By.id("android:id/progress");
    private static final By ACCEPT_ADDS = By.id(PKG + ":id/input_adds_check_box");
    private static final By VISIBLE_BUTTON = By.id(PKG + ":id/visibleButtonTest");
    private static final By TOAST_BUTTON = By.id(PKG + ":id/showToastButton");
    private static final By POPUP_BUTTON = By.id(PKG + ":id/showPopupWindowButton");
    private static final By EXCEPTION_BUTTON = By.id(PKG + ":id/exceptionTestButton");
    private static final By EXCEPTION_FIELD = By.id(PKG + ":id/exceptionTestField");
    private static final By TOP_LEVEL_ELEMENT = By.id(PKG + ":id/topLevelElementTest");

    private final Map<String, By> elements = new LinkedHashMap<>();

    public HomePage(AndroidDriver driver) {
        super(driver);
        elements.put("EN Button", EN_BUTTON);
        elements.put("Show Progress Bar for a while", WAITING_BUTTON);
        elements.put("I accept adds", ACCEPT_ADDS);
        elements.put("Display text view", VISIBLE_BUTTON);
        elements.put("Displays a Toast", TOAST_BUTTON);
        elements.put("Display Popup Window", POPUP_BUTTON);
        elements.put("Press to throw unhandled exception", EXCEPTION_BUTTON);
        elements.put("Display and focus on layout", TOP_LEVEL_ELEMENT);
    }

    public boolean isLoaded() {
        return isDisplayed(EN_BUTTON);
    }

    public String title() {
        return getText(TITLE);
    }

    public boolean isElementDisplayed(String element) {
        return isDisplayed(requireElement(element), 3);
    }

    public void tap(String element) {
        click(requireElement(element));
    }

    public void tapFileLogo() {
        click(START_REGISTRATION);
    }

    public void tapChromeLogo() {
        click(START_WEBVIEW);
    }

    public void typeIntoExceptionField(String text) {
        try {
            WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(EXCEPTION_FIELD));
            field.click();
            field.sendKeys(text);
        } catch (WebDriverException e) {
            // Expected in the S9 fail case: typing this text crashes the app and
            // the field goes stale; the home-title assertion still runs and fails.
        }
    }

    public void waitForProgressLoaderToDisappear() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(PROGRESS));
        } catch (TimeoutException e) {
            // Loader may already have finished.
        }
        wait.until(ExpectedConditions.invisibilityOfElementLocated(PROGRESS));
    }

    private By requireElement(String element) {
        By by = elements.get(element);
        if (by == null) {
            throw new IllegalArgumentException("Unknown home element: " + element);
        }
        return by;
    }
}
