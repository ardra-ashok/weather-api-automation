package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.*;
import services.WeatherService;
import specs.ApiHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class WeatherTest {

    ExtentReports extent;
    ExtentTest test;
    WeatherService weatherService = new WeatherService();

    /* Before class set up to initialize reporting */

    @BeforeClass(alwaysRun = true)
    public void setup() {
        if (extent == null) {
            ExtentSparkReporter spark =
                    new ExtentSparkReporter("reports/WeatherReport.html");
            extent = new ExtentReports();
            extent.attachReporter(spark);
        }
    }

    /* Test for retrieving the weather details for selected cities from the testData.json */

    @Test
    public void getWeather() throws IOException {

        test = extent.createTest("Retrieving weather details");
        String json = new String(
                Files.readAllBytes(Paths.get("src/supportData/testData.json"))
        );
        JsonPath jp = new JsonPath(json);
        List<String> cities = jp.getList("cities");

        for(String city : cities) {
            Response response = weatherService.getWeather(city,"");
            response.then()
                    .spec(ApiHandler.responseSpec());
            float temp = response.jsonPath().getFloat("data[0].app_temp");
            String description = response.jsonPath().getString("data[0].weather.description");
            System.out.println("City: " + city + ", Temp: " + temp + " degree Celcius, Status: "+ description);
        }
        test.pass("Weather retrieved successfully: ");

    }

    /* Test for retrieving the warmest city in Australia */

    @Test(groups = {"warmestCity_AU"})
    public void getWarmestAustralianCity() throws IOException {

        test = extent.createTest("Get the Warmest Australian city");
        float maxTemp = -100f;
        String warmestCity = "";
        String json = new String(
                Files.readAllBytes(Paths.get("src/supportData/testData.json")));
        JsonPath jp = new JsonPath(json);
        List<String> cities = jp.getList("cities_Australia");

        for(String city : cities) {
            Response response = weatherService.getWeather(city,"");
            response.then()
                    .spec(ApiHandler.responseSpec());
            float temp = response.jsonPath().getFloat("data[0].app_temp");
            if(temp>maxTemp){
                maxTemp = temp;
                warmestCity = city;
            }
        }
        System.out.println("Warmest City is " + warmestCity + " with temp: " + maxTemp + " degree Celcius");
        test.pass("Warmest Australian city retrieved successfully");
    }

    /* Test for retrieving the coldest state in USA */

    @Test(groups = {"coldestState_US"})
    public void getColdestUSCity() throws IOException {

        test = extent.createTest("Retrieving coldest state in US");
        Path path = Paths.get("src/supportData/states.csv");
        List<String> lines = Files.readAllLines(path);

        String coldestState = "";
        float minTemp = 100f;

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().isEmpty())
                continue;
            String[] columns = line.split(",");
            String stateName = columns[1].trim();
            String countryCode = columns[2].trim();
            if (countryCode.equalsIgnoreCase("US")) {
                Response response = weatherService.getWeather(stateName,"US");
                response.then().spec(ApiHandler.responseSpec());
                float currentTemp = response.jsonPath().getFloat("data[0].app_temp");
                if (currentTemp < minTemp) {
                    minTemp = currentTemp;
                    coldestState = stateName;
                }
            }
        }
        System.out.println("Coldest state in United States is: " + coldestState + "with Temp: " + minTemp + " degree Celcius");
        test.pass("Coldest US state retrieved successfully");
    }

    /* After class setup to teardown reporting */

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (extent != null) {
            extent.flush();
        }
    }
}
