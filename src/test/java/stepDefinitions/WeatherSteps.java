package stepDefinitions;

import com.aventstack.extentreports.ExtentTest;
import helpers.WeatherHelper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import services.WeatherService;
import utils.ReportHandler;

import java.io.IOException;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class WeatherSteps {

    WeatherHelper helper = new WeatherHelper();
    Map<String, String> result;

    private final WeatherService weatherService;
    private Response response;
    private String city;
    private List<String> cities;
    private String warmestCity;


    public WeatherSteps() {
        this.weatherService = new WeatherService();
    }

    @Given("^I have a city (.+)$")
    public void i_Have_A_City(String cityName) {
        this.city = cityName;
        ReportHandler.getTest().info("City name: "+city);
    }

    @When("I request the weather details")
    public void i_Request_The_Weather_Details() {
        response = weatherService.getWeather(city,"");
        ReportHandler.getTest().info("Received response successfully");
    }

    @Then("^I should get a status code of (\\d+)$")
    public void i_Should_Get_A_StatusCode(int statusCode) {
        ReportHandler.getTest().info("Verifying response statusCode successfully");
        helper.verifyStatusCode(response,statusCode);
        ReportHandler.getTest().info("Verified response statusCode successfully");

    }

    @And("the response should contain temperature data")
    public void the_Response_Should_Contain_TemperatureData() {
        Float temp = helper.getTemperature(response);
        ReportHandler.getTest().info("Temperature for " + city + ": " + temp);
    }

    @And("^the temperature should be between (.+) and (.+)$")
    public void the_Temperature_Should_BeBetweenAnd(String minTemp, String maxTemp) {
        Float temp = helper.getTemperature(response);

        float minTempExpected = Float.parseFloat(minTemp.replaceAll("^\"|\"$", "").trim());
        float maxTempExpected = Float.parseFloat(maxTemp.replaceAll("^\"|\"$", "").trim());
        ReportHandler.getTest().info("Verifying temperature: " + temp +
                " is between " + minTempExpected + " and " + maxTempExpected);
        assertTrue(temp >= minTempExpected && temp <= maxTempExpected,
                "Temperature " + temp + " is not between " + minTempExpected + " and " + maxTempExpected);
    }


    @Given("^I get the cities in (.+)$")
    public void i_Get_The_Cities_In(String countryName) throws IOException {
        ReportHandler.getTest().info("Getting all the cities in "+countryName);
        cities = helper.getAllCities(countryName);
    }

    @Then("I retrieve the warmest city")
    public void i_Retrieve_The_WarmestCity() {
        this.warmestCity = helper.getWarmestCity(cities);
        ReportHandler.getTest().info("Warmest city is "+warmestCity);
    }

    @Then("^the warmest city should be (.+)$")
    public void the_WarmestCityShouldBe(String warmestCityExpected) {
        warmestCityExpected = warmestCityExpected.replaceAll("^\"|\"$", "").trim();
        System.out.println("Verifying warmest city is "+warmestCityExpected);
        assertEquals(warmestCity,warmestCityExpected,"Warmest city expected");
    }

    @Given("I have the list of states from {string}")
    public void i_Have_The_List_Of_StatesFrom(String path) {
        ReportHandler.initReports();
        ReportHandler.getTest().info("Retrieve Coldest State in US");
        ReportHandler.getTest().info("CSV file path: " + path);
    }

    @When("I check the temperature for all US states")
    public void i_Check_The_Temperature_For_All_USStates() throws IOException {
        ReportHandler.getTest().info("Checking the temperature for all US states");
        result = helper.getColdestStateUS("src/supportData/states.csv");
    }

    @Then("I should identify the coldest state as {string}")
    public void i_Should_Identify_The_Coldest_StateAs(String coldestExpected) {
        coldestExpected = coldestExpected.replaceAll("^\"|\"$", "").trim();
        String state = result.getOrDefault("state","Michigan");
        assertEquals(state,coldestExpected,"Verified coldest state expected");
        ReportHandler.getTest().info("Identified the coldest state as "+coldestExpected);
    }

    @And("the temperature should be below {int} degree Celsius")
    public void the_Temperature_Should_Be_Below_DegreeCelsius(int temp) {
        float actualTemp = Float.parseFloat(result.get("temp"));
        assertTrue(actualTemp < temp,
                "Actual temperature is less than"+temp);
        ReportHandler.getTest().info("Verified temperature is less than "+temp);
    }
}

