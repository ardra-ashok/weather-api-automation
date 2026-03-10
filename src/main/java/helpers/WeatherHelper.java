package helpers;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import services.WeatherService;
import specs.ApiHandler;

import javax.sound.sampled.AudioSystem;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherHelper {

    private final WeatherService weatherService = new WeatherService();

    public Map<String, String> getColdestStateUS(String csvFilePath) throws IOException {
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
        Map<String, String> result = new HashMap<>();
        result.put("state", coldestState);
        result.put("temp", String.valueOf(minTemp));
        return result;

    }

    public Float getTemperature(Response response){
        return response.jsonPath().getFloat("data[0].app_temp");
    }

    public void verifyStatusCode(Response response,int statusCode) {
        response.then().statusCode(statusCode);
    }

    public List<String> getAllCities(String country) throws IOException {
        String json = new String(
                Files.readAllBytes(Paths.get("src/supportData/testData.json")));
        JsonPath jp = new JsonPath(json);
        return jp.getList("cities_Australia");
    }

    public String getWarmestCity(List<String> cities){

        String warmestCity = "";
        float maxTemp = -100f;
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
        return warmestCity;
    }

}
