@web
Feature: jQuery UI interactions

  @case1
  Scenario: Droppable - drop an element on the target
    Given I open the "Droppable" demo
    When I drag the draggable onto the droppable
    Then the droppable shows "Dropped!"

  @case2
  Scenario: Selectable - select multiple items
    Given I open the "Selectable" demo
    When I select items "Item 1", "Item 3" and "Item 7"
    Then the selected items are "Item 1", "Item 3" and "Item 7"

  @case3
  Scenario: Controlgroup - set values and click Book Now
    Given I open the "Controlgroup" demo
    When I set the horizontal controlgroup to car "SUV", transmission "Automatic", insurance on and "2" cars
    And I set the vertical controlgroup to car "Truck", transmission "Standard", insurance on and "1" cars
    And I click Book Now in the horizontal controlgroup
    And I click Book Now in the vertical controlgroup
    Then the horizontal controlgroup has car "SUV", transmission "Automatic", insurance on and "2" cars
    And the vertical controlgroup has car "Truck", transmission "Standard", insurance on and "1" cars

  @case4
  Scenario: Datepicker - pick today
    Given I open the "Datepicker" demo
    When I pick today in the datepicker
    Then the datepicker shows today

  @case5
  Scenario: Resizable - enlarge the box
    Given I open the "Resizable" demo
    When I resize the resizable box by "100" and "100"
    Then the resizable box is larger than before

  @case6
  Scenario: Sortable - reverse the order
    Given I open the "Sortable" demo
    When I reverse the sortable order
    Then the sortable order is reversed

  @case7
  Scenario: Widget Factory - go green
    Given I open the "Widget Factory" demo
    When I click "Go green" in the demo
    Then the first widget color is "rgb(64, 250, 8)"
