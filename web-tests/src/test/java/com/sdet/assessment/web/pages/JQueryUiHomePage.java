package com.sdet.assessment.web.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.sdet.assessment.web.config.ConfigReader;

public class JQueryUiHomePage {

    private final Page page;
    private final String baseUrl;

    public JQueryUiHomePage(Page page) {
        this.page = page;
        this.baseUrl = ConfigReader.get("base.url");
    }

    public void open() {
        page.navigate(baseUrl);
    }

    public void openDemo(String linkText) {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(linkText).setExact(true))
                .first()
                .click();
        page.locator("iframe.demo-frame").waitFor();
    }
}
