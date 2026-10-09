package com.sdet.assessment.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class PopupWindowPage extends BasePage {

    private static final By TITLE = By.xpath("//android.widget.TextView[@text=\"It's a PopupWindow\"]");
    private static final By DISMISS = By.id("io.selendroid.testapp:id/popup_dismiss_button");

    public PopupWindowPage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return isDisplayed(TITLE, 5);
    }

    public String title() {
        return getText(TITLE);
    }

    public void dismiss() {
        click(DISMISS);
    }

    public boolean isDismissed() {
        return waitUntilNotDisplayed(DISMISS);
    }
}
