package com.sdet.assessment.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ToastPage extends BasePage {

    private static final By TOAST = By.xpath("//android.widget.Toast");

    public ToastPage(AndroidDriver driver) {
        super(driver);
    }

    public String waitForText() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(TOAST)).getText();
    }
}
