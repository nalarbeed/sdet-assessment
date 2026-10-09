package com.sdet.assessment.web.pages;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Page;

public class DatepickerPage {

    private final Page page;

    public DatepickerPage(Page page) {
        this.page = page;
    }

    private FrameLocator frame() {
        return page.frameLocator(Locators.DEMO_FRAME);
    }

    public void pickToday() {
        frame().locator("#datepicker").click();
        frame().locator(".ui-datepicker-today a").click();
    }

    public String value() {
        return frame().locator("#datepicker").inputValue();
    }
}
