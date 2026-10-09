package com.sdet.assessment.web.hooks;

import com.microsoft.playwright.Page;
import com.sdet.assessment.web.driver.PlaywrightFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.qameta.allure.Allure;
import io.qameta.allure.AttachmentOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;

public class Hooks {

    private static final Logger log = LoggerFactory.getLogger(Hooks.class);

    @Before
    public void setUp() {
        PlaywrightFactory.init();
    }

    @After
    public void tearDown() {
        Page page = PlaywrightFactory.getPage();
        if (page != null) {
            try {
                byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
                Allure.attachment("Screenshot", "image/png", new ByteArrayInputStream(screenshot),
                        AttachmentOptions.withFileExtension("png"));
            } catch (Exception e) {
                log.warn("Could not capture screenshot: {}", e.getMessage());
            }
        }
        PlaywrightFactory.close();
    }
}
