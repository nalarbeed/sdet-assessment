package com.sdet.assessment.web.pages;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ControlgroupPage {

    private final Page page;

    public ControlgroupPage(Page page) {
        this.page = page;
    }

    private FrameLocator frame() {
        return page.frameLocator(Locators.DEMO_FRAME);
    }

    public void setHorizontal(String car, String transmission, String cars) {
        selectCar(frame().locator("#car-type-button"), car);
        clickLabel("transmission-" + transmission.toLowerCase());
        clickLabel("insurance");
        frame().locator("#horizontal-spinner").fill(cars);
    }

    public void setVertical(String car, String transmission, String cars) {
        selectCar(frame().locator(".controlgroup-vertical .ui-selectmenu-button"), car);
        clickLabel("transmission-" + transmission.toLowerCase() + "-v");
        clickLabel("insurance-v");
        frame().locator("#vertical-spinner").fill(cars);
    }

    public void clickHorizontalBookNow() {
        frame().locator(".controlgroup button").first().click();
    }

    public void clickVerticalBookNow() {
        frame().locator("#book").click();
    }

    public String horizontalCar() {
        return frame().locator("#car-type-button .ui-selectmenu-text").innerText();
    }

    public String verticalCar() {
        return frame().locator(".controlgroup-vertical .ui-selectmenu-text").innerText();
    }

    public boolean horizontalTransmission(String transmission) {
        return frame().locator("#transmission-" + transmission.toLowerCase()).isChecked();
    }

    public boolean verticalTransmission(String transmission) {
        return frame().locator("#transmission-" + transmission.toLowerCase() + "-v").isChecked();
    }

    public boolean horizontalInsurance() {
        return frame().locator("#insurance").isChecked();
    }

    public boolean verticalInsurance() {
        return frame().locator("#insurance-v").isChecked();
    }

    public String horizontalCars() {
        return frame().locator("#horizontal-spinner").inputValue();
    }

    public String verticalCars() {
        return frame().locator("#vertical-spinner").inputValue();
    }

    private void selectCar(Locator button, String car) {
        button.click();
        frame().locator(".ui-selectmenu-open .ui-menu-item")
                .filter(new Locator.FilterOptions().setHasText(car))
                .first()
                .click();
    }

    private void clickLabel(String forId) {
        frame().locator("label[for='" + forId + "']").click();
    }
}
