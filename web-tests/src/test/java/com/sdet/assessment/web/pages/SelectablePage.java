package com.sdet.assessment.web.pages;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.KeyboardModifier;

import java.util.List;

public class SelectablePage {

    private final Page page;

    public SelectablePage(Page page) {
        this.page = page;
    }

    private FrameLocator frame() {
        return page.frameLocator(Locators.DEMO_FRAME);
    }

    private Locator item(String text) {
        return frame().locator("#selectable li")
                .filter(new Locator.FilterOptions().setHasText(text));
    }

    public void selectItems(String... items) {
        for (String text : items) {
            item(text).click(new Locator.ClickOptions().setModifiers(List.of(KeyboardModifier.CONTROL)));
        }
    }

    public List<String> selectedItems() {
        return frame().locator("#selectable li.ui-selected").allInnerTexts();
    }
}
