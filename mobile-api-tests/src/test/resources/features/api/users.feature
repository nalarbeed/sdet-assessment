@api
Feature: ReqRes users API

  Scenario: Get users on page 2
    Given the ReqRes API is available
    When I request page 2 of users
    Then the response status is 200
    And the first name of user with id 10 is "Byron"

  Scenario: Create a user chaining from the page 2 response
    Given the ReqRes API is available
    When I request page 2 of users
    Then the response status is 200
    When I create a user from the first name of user with id 10 and the configured job
    Then the response status is 201
    And the generated id is not empty
    And the response matches the JSON schema "schemas/create-user-schema.json"
