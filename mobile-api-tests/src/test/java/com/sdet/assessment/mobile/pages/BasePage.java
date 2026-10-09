package com.sdet.assessment.mobile.pages;

import com.sdet.assessment.mobile.config.ConfigReader;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {

    protected final AndroidDriver driver;
    protected final WebDriverWait wait;
    protected final long timeoutSeconds;

    public BasePage(AndroidDriver driver) {
        this.driver = driver;
        this.timeoutSeconds = Long.parseLong(ConfigReader.get("appium.wait.timeout"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    protected void click(By by) {
        wait.until(ExpectedConditions.elementToBeClickable(by)).click();
    }

    protected String getText(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by)).getText();
    }

    protected boolean isDisplayed(By by) {
        return isDisplayed(by, timeoutSeconds);
    }

    protected boolean isDisplayed(By by, long seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .until(ExpectedConditions.visibilityOfElementLocated(by));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected boolean waitUntilNotDisplayed(By by) {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected WebElement find(By by) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }
}
