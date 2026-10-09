package com.sdet.assessment.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class LocalizationPage extends BasePage {

    private static final By MESSAGE = By.id("android:id/message");
    private static final By AGREE = By.id("android:id/button1");
    private static final By NO_NO = By.id("android:id/button2");

    public LocalizationPage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return isDisplayed(MESSAGE, 5);
    }

    public String message() {
        return getText(MESSAGE);
    }

    public void choose(String option) {
        switch (option) {
            case "I agree":
                click(AGREE);
                break;
            case "No, no":
                click(NO_NO);
                break;
            default:
                throw new IllegalArgumentException("Unknown localization dialog option: " + option);
        }
    }
}
