Feature: A sample behavior driven framework to assess Budget website
  
  Scenario: TC_001:Select a cheapest SUV car and verify that the journey details and details of selected car are carried over as expected
    When Enter and submit the journey details on home page
    And Verify that the journey details are carried over as expected on select vehicle page
    And Refine the search results by SUV and cheaper
    And Capture and click on "Pay Now" for the cheapest car from the search results
    Then Verify that the journey details and details of selected car are carried over as expected on extras page
    And Verify that the components of rental cost are as expected
    