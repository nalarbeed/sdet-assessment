package com.sdet.assessment.web.pages;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Mouse;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;

import java.util.List;

public class SortablePage {

    private final Page page;

    public SortablePage(Page page) {
        this.page = page;
    }

    private FrameLocator frame() {
        return page.frameLocator("iframe.demo-frame");
    }

    public List<String> items() {
        Locator items = frame().locator("#sortable li");
        // Web-first wait: wait for the list to be rendered before reading, so the
        // original order is never captured as empty.
        items.first().waitFor();
        return items.allInnerTexts();
    }

    public void reverse() {
        int count = items().size();
        Locator first = frame().locator("#sortable li")
                .filter(new Locator.FilterOptions().setHasText("Item 1"))
                .first();
        for (int n = count; n >= 2; n--) {
            Locator source = frame().locator("#sortable li")
                    .filter(new Locator.FilterOptions().setHasText("Item " + n))
                    .first();
            drag(source, first);
        }
    }

    private void drag(Locator source, Locator target) {
        source.scrollIntoViewIfNeeded();
        BoundingBox s = source.boundingBox();
        BoundingBox t = target.boundingBox();
        page.mouse().move(s.x + s.width / 2, s.y + s.height / 2);
        page.mouse().down();
        page.mouse().move(t.x + t.width / 2, t.y + t.height / 2, new Mouse.MoveOptions().setSteps(10));
        page.mouse().up();
    }
}
