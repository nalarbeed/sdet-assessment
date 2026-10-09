package com.sdet.assessment.mobile.hooks;

import com.sdet.assessment.mobile.config.ConfigReader;
import com.sdet.assessment.mobile.driver.AppiumDriverManager;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.qameta.allure.Allure;
import io.qameta.allure.AttachmentOptions;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.time.Duration;

public class MobileHooks {

    private static final Logger log = LoggerFactory.getLogger(MobileHooks.class);

    private static final String APP_PACKAGE = "io.selendroid.testapp";
    private static final String APP_ACTIVITY = "io.selendroid.testapp.HomeScreenActivity";

    private static final By HOME = By.id(APP_PACKAGE + ":id/buttonTest");
    private static final By REVIEW_CONTINUE =
            By.id("com.android.permissioncontroller:id/continue_button");
    private static final By DEPRECATED_OK = By.id("android:id/button1");

    @Before("@mobile")
    public void setUp() {
        AppiumDriverManager.start();
        AndroidDriver driver = AppiumDriverManager.get();
        enableMultiWindowAccess(driver);
        launchApp(driver);
        dismissSystemDialogs(driver);

        int timeout = Integer.parseInt(ConfigReader.get("appium.wait.timeout"));
        new WebDriverWait(driver, Duration.ofSeconds(timeout))
                .until(ExpectedConditions.visibilityOfElementLocated(HOME));
        log.info("Mobile app home screen is ready");
    }

    @After("@mobile")
    public void tearDown() {
        AndroidDriver driver = AppiumDriverManager.get();
        if (driver != null) {
            try {
                byte[] screenshot = driver.getScreenshotAs(OutputType.BYTES);
                Allure.attachment("Screenshot", "image/png",
                        new ByteArrayInputStream(screenshot),
                        AttachmentOptions.withFileExtension("png"));
            } catch (Exception e) {
                log.warn("Could not capture screenshot: {}", e.getMessage());
            }
        }
        AppiumDriverManager.quit();
    }

    private void enableMultiWindowAccess(AndroidDriver driver) {
        driver.setSetting("includeSiblingWindows", true);
        driver.setSetting("enableMultiWindows", true);
        driver.setSetting("limitXPathContextScope", false);
    }

    private void launchApp(AndroidDriver driver) {
        driver.activateApp(APP_PACKAGE);
        log.info("Launched {}", APP_PACKAGE);
    }

    private void dismissSystemDialogs(AndroidDriver driver) {
        // This legacy app triggers two blocking system dialogs on launch:
        //  - the permission-review dialog ("Choose what to allow ...")
        //  - the "This app was built for an older version of Android" warning
        // Dismiss each only if it actually appears.
        dismissIfPresent(driver, REVIEW_CONTINUE, "permission review");
        dismissIfPresent(driver, DEPRECATED_OK, "older-version warning");
        if (!isHomeVisible(driver)) {
            dismissIfPresent(driver, REVIEW_CONTINUE, "permission review");
            dismissIfPresent(driver, DEPRECATED_OK, "older-version warning");
        }
    }

    private void dismissIfPresent(AndroidDriver driver, By by, String what) {
        try {
            WebElement element = new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.elementToBeClickable(by));
            element.click();
            log.info("Dismissed {} dialog", what);
        } catch (TimeoutException ignored) {
            // Dialog is not present; nothing to do.
        }
    }

    private boolean isHomeVisible(AndroidDriver driver) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(ExpectedConditions.visibilityOfElementLocated(HOME));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
