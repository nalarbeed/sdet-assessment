package com.sdet.assessment.web.steps;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import com.sdet.assessment.web.driver.PlaywrightFactory;
import com.sdet.assessment.web.pages.ControlgroupPage;
import com.sdet.assessment.web.pages.DatepickerPage;
import com.sdet.assessment.web.pages.DroppablePage;
import com.sdet.assessment.web.pages.JQueryUiHomePage;
import com.sdet.assessment.web.pages.ResizablePage;
import com.sdet.assessment.web.pages.SelectablePage;
import com.sdet.assessment.web.pages.SortablePage;
import com.sdet.assessment.web.pages.WidgetFactoryPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WebSteps {

    private static final Logger log = LoggerFactory.getLogger(WebSteps.class);

    private static final int SORTABLE_ITEM_COUNT = 7;

    private Page page;
    private JQueryUiHomePage home;
    private DroppablePage droppablePage;
    private SelectablePage selectablePage;
    private ControlgroupPage controlgroupPage;
    private DatepickerPage datepickerPage;
    private ResizablePage resizablePage;
    private SortablePage sortablePage;
    private WidgetFactoryPage widgetFactoryPage;

    private BoundingBox previousBox;
    private List<String> previousOrder;

    @Given("I open the {string} demo")
    public void iOpenTheDemo(String linkText) {
        page = PlaywrightFactory.getPage();
        home = new JQueryUiHomePage(page);
        home.open();
        log.info("Opening '{}' demo", linkText);
        home.openDemo(linkText);
    }

    @When("I drag the draggable onto the droppable")
    public void iDragTheDraggableOntoTheDroppable() {
        droppablePage = new DroppablePage(page);
        log.info("Dragging draggable onto droppable");
        droppablePage.dragToTarget();
    }

    @Then("the droppable shows {string}")
    public void theDroppableShows(String expected) {
        Assert.assertEquals(droppablePage.targetText(), expected, "Unexpected droppable text");
    }

    @When("I select items {string}, {string} and {string}")
    public void iSelectItems(String first, String second, String third) {
        selectablePage = new SelectablePage(page);
        log.info("Selecting items: {}, {}, {}", first, second, third);
        selectablePage.selectItems(first, second, third);
    }

    @Then("the selected items are {string}, {string} and {string}")
    public void theSelectedItemsAre(String first, String second, String third) {
        Assert.assertEquals(selectablePage.selectedItems(), List.of(first, second, third),
                "Unexpected selection");
    }

    @When("I set the horizontal controlgroup to car {string}, transmission {string}, insurance on and {string} cars")
    public void iSetTheHorizontalControlgroup(String car, String transmission, String cars) {
        controlgroupPage = new ControlgroupPage(page);
        log.info("Horizontal controlgroup: car={}, transmission={}, cars={}", car, transmission, cars);
        controlgroupPage.setHorizontal(car, transmission, cars);
    }

    @When("I set the vertical controlgroup to car {string}, transmission {string}, insurance on and {string} cars")
    public void iSetTheVerticalControlgroup(String car, String transmission, String cars) {
        controlgroupPage = new ControlgroupPage(page);
        log.info("Vertical controlgroup: car={}, transmission={}, cars={}", car, transmission, cars);
        controlgroupPage.setVertical(car, transmission, cars);
    }

    @When("I click Book Now in the horizontal controlgroup")
    public void iClickBookNowHorizontal() {
        log.info("Clicking Book Now (horizontal)");
        controlgroupPage.clickHorizontalBookNow();
    }

    @When("I click Book Now in the vertical controlgroup")
    public void iClickBookNowVertical() {
        log.info("Clicking Book Now (vertical)");
        controlgroupPage.clickVerticalBookNow();
    }

    @Then("the horizontal controlgroup has car {string}, transmission {string}, insurance on and {string} cars")
    public void theHorizontalControlgroupHas(String car, String transmission, String cars) {
        Assert.assertEquals(controlgroupPage.horizontalCar(), car, "Unexpected car");
        Assert.assertTrue(controlgroupPage.horizontalTransmission(transmission),
                "Transmission not selected: " + transmission);
        Assert.assertTrue(controlgroupPage.horizontalInsurance(), "Insurance not checked");
        Assert.assertEquals(controlgroupPage.horizontalCars(), cars, "Unexpected # of cars");
    }

    @Then("the vertical controlgroup has car {string}, transmission {string}, insurance on and {string} cars")
    public void theVerticalControlgroupHas(String car, String transmission, String cars) {
        Assert.assertEquals(controlgroupPage.verticalCar(), car, "Unexpected car");
        Assert.assertTrue(controlgroupPage.verticalTransmission(transmission),
                "Transmission not selected: " + transmission);
        Assert.assertTrue(controlgroupPage.verticalInsurance(), "Insurance not checked");
        Assert.assertEquals(controlgroupPage.verticalCars(), cars, "Unexpected # of cars");
    }

    @When("I pick today in the datepicker")
    public void iPickTodayInTheDatepicker() {
        datepickerPage = new DatepickerPage(page);
        log.info("Picking today's date");
        datepickerPage.pickToday();
    }

    @Then("the datepicker shows today")
    public void theDatepickerShowsToday() {
        String expected = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        Assert.assertEquals(datepickerPage.value(), expected, "Unexpected date value");
    }

    @When("I resize the resizable box by {string} and {string}")
    public void iResizeTheResizableBox(String dx, String dy) {
        resizablePage = new ResizablePage(page);
        previousBox = resizablePage.boxSize();
        log.info("Resizing box by ({}, {})", dx, dy);
        resizablePage.resizeBy(Integer.parseInt(dx), Integer.parseInt(dy));
    }

    @Then("the resizable box is larger than before")
    public void theResizableBoxIsLarger() {
        BoundingBox now = resizablePage.boxSize();
        Assert.assertTrue(now.width > previousBox.width, "Width did not increase");
        Assert.assertTrue(now.height > previousBox.height, "Height did not increase");
    }

    @When("I reverse the sortable order")
    public void iReverseTheSortableOrder() {
        sortablePage = new SortablePage(page);
        previousOrder = sortablePage.items();
        log.info("Sortable order before: {}", previousOrder);
        sortablePage.reverse();
        log.info("Sortable order after: {}", sortablePage.items());
    }

    @Then("the sortable order is reversed")
    public void theSortableOrderIsReversed() {
        Assert.assertNotNull(previousOrder, "Original sortable order was not captured");
        Assert.assertFalse(previousOrder.isEmpty(), "Original sortable order is empty");
        Assert.assertEquals(previousOrder.size(), SORTABLE_ITEM_COUNT,
                "Expected " + SORTABLE_ITEM_COUNT + " sortable items before reversing");
        List<String> expected = new ArrayList<>(previousOrder);
        Collections.reverse(expected);
        List<String> actual = sortablePage.items();
        log.info("Expected reversed order: {}", expected);
        log.info("Actual sortable order: {}", actual);
        Assert.assertEquals(actual, expected, "Sortable order not reversed");
    }

    @When("I click {string} in the demo")
    public void iClickInTheDemo(String name) {
        widgetFactoryPage = new WidgetFactoryPage(page);
        log.info("Clicking '{}'", name);
        widgetFactoryPage.clickButton(name);
    }

    @Then("the first widget color is {string}")
    public void theFirstWidgetColorIs(String expected) {
        Assert.assertEquals(widgetFactoryPage.firstWidgetColor(), expected, "Unexpected widget color");
    }
}
