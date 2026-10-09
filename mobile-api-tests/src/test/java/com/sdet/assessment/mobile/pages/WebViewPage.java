package com.sdet.assessment.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WebViewPage extends BasePage {

    private static final By BODY = By.tagName("body");
    private static final By NAME_INPUT = By.cssSelector("#name_input");
    private static final By CAR_SELECT = By.cssSelector("select[name='car']");
    private static final By SUBMIT = By.cssSelector("input[type='submit']");

    public WebViewPage(AndroidDriver driver) {
        super(driver);
    }

    public void switchToWebView() {
        // The WEBVIEW_ context only appears once the page has loaded.
        String context = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ignored -> findWebViewContext());
        driver.context(context);
    }

    private String findWebViewContext() {
        return driver.getContextHandles().stream()
                .filter(c -> c.startsWith("WEBVIEW_"))
                .findFirst()
                .orElse(null);
    }

    public void switchToNative() {
        driver.context("NATIVE_APP");
    }

    public String title() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(BODY));
        return driver.getTitle();
    }

    public String bodyText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(BODY)).getText();
    }

    public void enterName(String name) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(NAME_INPUT));
        input.clear();
        input.sendKeys(name);
    }

    public void selectCar(String car) {
        new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(CAR_SELECT)))
                .selectByVisibleText(car);
    }

    public void submit() {
        click(SUBMIT);
    }

    public void clickLink(String linkText) {
        click(By.linkText(linkText));
    }

    public String defaultCar() {
        return new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(CAR_SELECT)))
                .getFirstSelectedOption().getText();
    }
}
