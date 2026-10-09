@mobile
Feature: Selendroid test app mobile scenarios

  @s1
  Scenario: S1 Launch the app and verify the home screen
    Given I am on the home screen
    Then the title is "selendroid-test-app"
    And the home screen shows the elements
      | EN Button                     |
      | Show Progress Bar for a while |
      | I accept adds                 |
      | Display text view             |
      | Displays a Toast              |
      | Display Popup Window          |
      | Display and focus on layout   |

  @s2
  Scenario: S2 Localization EN button - choose No, no
    Given I am on the home screen
    When I tap the "EN Button" button
    Then the localization dialog shows message "This will end the activity"
    When I choose "No, no"
    Then the home screen is displayed

  @s6
  Scenario: S6 Displays a Toast
    Given I am on the home screen
    When I tap the "Displays a Toast" button
    Then the toast text is "Hello selendroid toast!"

  @s7
  Scenario: S7 Display Popup Window and dismiss it
    Given I am on the home screen
    When I tap the "Display Popup Window" button
    Then the popup window is displayed
    When I dismiss the popup window
    Then the popup window is gone
