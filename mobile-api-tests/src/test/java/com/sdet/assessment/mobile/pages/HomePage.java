package com.sdet.assessment.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

import java.util.LinkedHashMap;
import java.util.Map;

public class HomePage extends BasePage {

    private static final String PKG = "io.selendroid.testapp";

    private static final By TITLE = By.id("android:id/title");
    private static final By HEADING = By.xpath("//android.widget.TextView[@text='Hello Default Locale, Selendroid-test-app!']");
    private static final By EN_BUTTON = By.id(PKG + ":id/buttonTest");
    private static final By START_WEBVIEW = By.id(PKG + ":id/buttonStartWebview");
    private static final By START_REGISTRATION = By.id(PKG + ":id/startUserRegistration");
    private static final By TEXT_FIELD = By.id(PKG + ":id/my_text_field");
    private static final By WAITING_BUTTON = By.id(PKG + ":id/waitingButtonTest");
    private static final By ACCEPT_ADDS = By.id(PKG + ":id/input_adds_check_box");
    private static final By VISIBLE_BUTTON = By.id(PKG + ":id/visibleButtonTest");
    private static final By TOAST_BUTTON = By.id(PKG + ":id/showToastButton");
    private static final By POPUP_BUTTON = By.id(PKG + ":id/showPopupWindowButton");
    private static final By EXCEPTION_BUTTON = By.id(PKG + ":id/exceptionTestButton");
    private static final By EXCEPTION_FIELD = By.id(PKG + ":id/exceptionTestField");
    private static final By ENCODING_TEXT = By.id(PKG + ":id/encodingTextview");
    private static final By TOP_LEVEL_ELEMENT = By.id(PKG + ":id/topLevelElementTest");

    private final Map<String, By> elements = new LinkedHashMap<>();

    public HomePage(AndroidDriver driver) {
        super(driver);
        elements.put("selendroid-test-app", TITLE);
        elements.put("Hello Default Locale, Selendroid-test-app!", HEADING);
        elements.put("EN Button", EN_BUTTON);
        elements.put("Show Progress Bar for a while", WAITING_BUTTON);
        elements.put("I accept adds", ACCEPT_ADDS);
        elements.put("Display text view", VISIBLE_BUTTON);
        elements.put("Displays a Toast", TOAST_BUTTON);
        elements.put("Display Popup Window", POPUP_BUTTON);
        elements.put("Press to throw unhandled exception", EXCEPTION_BUTTON);
        elements.put("Display and focus on layout", TOP_LEVEL_ELEMENT);
        elements.put("buttonStartWebview", START_WEBVIEW);
        elements.put("startUserRegistration", START_REGISTRATION);
        elements.put("my_text_field", TEXT_FIELD);
        elements.put("exceptionTestField", EXCEPTION_FIELD);
        elements.put("encodingTextview", ENCODING_TEXT);
    }

    public boolean isLoaded() {
        return isDisplayed(EN_BUTTON);
    }

    public String title() {
        return getText(TITLE);
    }

    public boolean isElementDisplayed(String element) {
        By by = elements.get(element);
        if (by == null) {
            throw new IllegalArgumentException("Unknown home element: " + element);
        }
        return isDisplayed(by, 3);
    }

    public void tap(String element) {
        By by = elements.get(element);
        if (by == null) {
            throw new IllegalArgumentException("Unknown home element: " + element);
        }
        click(by);
    }
}
