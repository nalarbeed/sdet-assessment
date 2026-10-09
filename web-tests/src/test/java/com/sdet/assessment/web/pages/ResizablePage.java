package com.sdet.assessment.web.pages;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Mouse;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;

public class ResizablePage {

    private final Page page;

    public ResizablePage(Page page) {
        this.page = page;
    }

    private FrameLocator frame() {
        return page.frameLocator(Locators.DEMO_FRAME);
    }

    public BoundingBox boxSize() {
        return frame().locator("#resizable").boundingBox();
    }

    public void resizeBy(int dx, int dy) {
        Locator handle = frame().locator("#resizable .ui-resizable-se");
        handle.scrollIntoViewIfNeeded();
        BoundingBox h = handle.boundingBox();
        double startX = h.x + h.width / 2;
        double startY = h.y + h.height / 2;
        page.mouse().move(startX, startY);
        page.mouse().down();
        page.mouse().move(startX + dx, startY + dy, new Mouse.MoveOptions().setSteps(10));
        page.mouse().up();
    }
}
