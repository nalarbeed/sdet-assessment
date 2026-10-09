package com.sdet.assessment.web.pages;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Mouse;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;

public class DroppablePage {

    private final Page page;

    public DroppablePage(Page page) {
        this.page = page;
    }

    private FrameLocator frame() {
        return page.frameLocator("iframe.demo-frame");
    }

    public void dragToTarget() {
        drag(frame().locator("#draggable"), frame().locator("#droppable"));
    }

    public String targetText() {
        return frame().locator("#droppable p").innerText();
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
