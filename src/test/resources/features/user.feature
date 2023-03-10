
Feature: User Verification


  Scenario: verify information about logged user
    Given I logged Bookit api using as a "team-leader"
    When I sent get get request to "/api/users/me" endpoint
    Then status code should be 200
    And content type is "application/json"
    And role is "student-team-leader"

  # API vs DB --> two point verification

  Scenario: verify information about logged user from api and database
    Given I logged Bookit api using as a "team-leader"
    When I sent get get request to "/api/users/me" endpoint
    Then the information about current user from api and database should match


    # API vs DB vs UI --> three point verification
  @wip@db@ui
  Scenario: three point/layer (UI/API/DATABASE)
    Given user logs in using "team-leader" credentials
    And user is on the my self page
    And I logged Bookit api using as a "team-leader"
    When I sent get get request to "/api/users/me" endpoint
    Then UI, API and Database user information must be match


