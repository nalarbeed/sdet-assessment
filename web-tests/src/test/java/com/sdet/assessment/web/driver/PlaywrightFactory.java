package com.sdet.assessment.web.driver;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.sdet.assessment.web.config.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PlaywrightFactory {

    private static final Logger log = LoggerFactory.getLogger(PlaywrightFactory.class);

    private static final ThreadLocal<Playwright> PLAYWRIGHT = new ThreadLocal<>();
    private static final ThreadLocal<Browser> BROWSER = new ThreadLocal<>();
    private static final ThreadLocal<Page> PAGE = new ThreadLocal<>();

    private PlaywrightFactory() {
    }

    public static void init() {
        boolean headless = ConfigReader.getBoolean("headless");
        log.info("Launching Chromium (headless={})", headless);

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
        Page page = browser.newPage();

        PLAYWRIGHT.set(playwright);
        BROWSER.set(browser);
        PAGE.set(page);
    }

    public static Page getPage() {
        return PAGE.get();
    }

    public static void close() {
        Page page = PAGE.get();
        if (page != null) {
            page.close();
        }
        Browser browser = BROWSER.get();
        if (browser != null) {
            browser.close();
        }
        Playwright playwright = PLAYWRIGHT.get();
        if (playwright != null) {
            playwright.close();
        }
        PAGE.remove();
        BROWSER.remove();
        PLAYWRIGHT.remove();
    }
}
