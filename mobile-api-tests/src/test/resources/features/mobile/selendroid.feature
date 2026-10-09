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

  @s3
  Scenario: S3 Web view interaction - Say Hello
    Given I am on the home screen
    When I tap the Chrome logo button
    And I switch to the web view
    Then the web view title is "Say Hello Demo"
    And the web view shows text starting with "Hello, can you"
    When I enter the name "Homer Simpson" in the web view
    And I select the Preferred Car "Mercedes"
    And I tap "Send me your name!" in the web view
    Then the web view shows text starting with "This is my"
    And the web view shows the name "Homer Simpson"
    And the web view shows the preferred car "Mercedes"
    When I click the "here" link
    Then the default Preferred Car is "Volvo"
    And I switch back to the native view

  @s5
  Scenario: S5 Show progress bar then verify the registration screen
    Given I am on the home screen
    When I tap the "Show Progress Bar for a while" button
    And I wait for the progress loader to disappear
    Then the registration screen title is "selendroid-test-app"
    And the registration screen shows text starting with "Welcome to register"
    And the registration screen shows these elements
      | Username             |
      | E-Mail               |
      | Password             |
      | Name                 |
      | Programming Language |
      | I accept adds        |
      | Register User        |

  @s4
  Scenario: S4 Register a new user from the file logo button
    Given I am on the home screen
    When I tap the file logo button
    Then the registration screen title is "selendroid-test-app"
    And the registration screen shows text starting with "Welcome to register"
    And the registration screen shows these elements
      | Username             |
      | E-Mail               |
      | Password             |
      | Name                 |
      | Programming Language |
      | I accept adds        |
      | Register User        |
    And the Name field is "Mr. Burns"
    And the default Programming Language is "Ruby"
    When I fill the registration form with:
      | Field    | Value                |
      | Username | tester01             |
      | E-Mail   | tester01@example.com |
      | Password | secret               |
      | Name     | Homer Simpson        |
    And I select the Programming Language "Java"
    And I accept adds
    And I tap Register User
    Then the verify screen shows the registered user with:
      | Field                | Value                |
      | Name                 | Homer Simpson        |
      | Username             | tester01             |
      | Password             | secret               |
      | E-Mail               | tester01@example.com |
      | Programming Language | Java                 |
      | I accept adds        | true                 |
    When I tap Register User again
    Then the home screen is displayed

  @s8
  @fail-case
  Scenario: S8 Press to throw unhandled exception
    Given I am on the home screen
    When I tap the "Press to throw unhandled exception" button
    Then the home screen title is displayed

  @s9
  @fail-case
  Scenario: S9 Type to throw unhandled exception
    Given I am on the home screen
    When I type "test" in the exception field
    Then the home screen title is displayed
