package com.sdet.assessment.web.pages;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class WidgetFactoryPage {

    private final Page page;

    public WidgetFactoryPage(Page page) {
        this.page = page;
    }

    private FrameLocator frame() {
        return page.frameLocator(Locators.DEMO_FRAME);
    }

    public void clickButton(String name) {
        frame().getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName(name)).click();
    }

    public String firstWidgetColor() {
        Object color = frame().locator("#my-widget1")
                .evaluate("el => getComputedStyle(el).backgroundColor");
        return String.valueOf(color);
    }
}
