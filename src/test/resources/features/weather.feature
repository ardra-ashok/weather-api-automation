




  Feature: Weather API

#    /* Test for retrieving the weather details for selected cities and checking the range */

    Scenario Outline: Retrieve weather details for cities
      Given I have a city "<city>"
      When I request the weather details
      Then I should get a status code of 200
      And the response should contain temperature data
      And the temperature should be between "<minTemp>" and "<maxTemp>"
      Examples:
        | city     | minTemp | maxTemp |
        | Sydney   | 10      | 30      |
        | London   | 2       | 25      |
        | New York | -5      | 30      |
        | Dubai    | 18      | 45      |
        | Paris    | 3       | 27      |

#   /* Test for retrieving the warmest city in Australia */

    Scenario: Retrieve warmest city in Australia
      Given I get the cities in "Australia"
      Then I retrieve the warmest city
      Then the warmest city should be "Darwin"


#    /* Test for retrieving the coldest state in USA */

    Scenario: Get the coldest state in US
      Given I have the list of states from "src/supportData/states.csv"
      When I check the temperature for all US states
      Then I should identify the coldest state as "Michigan"
      And the temperature should be below 100 degree Celsius






