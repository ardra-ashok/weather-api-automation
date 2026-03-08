package services;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import specs.ApiHandler;

import java.util.HashMap;
import java.util.Map;

public class WeatherService {

    /* Method to get the weather data from the api*/

    public Response getWeather(String city, String countryCode) {

        Map<String, String> params = new HashMap<>();
        params.put("city", city);
        if (countryCode != null && !countryCode.trim().isEmpty()) {
            params.put("country", countryCode);
        }

        return given()
                .spec(ApiHandler.requestSpec())
                .queryParams(params)
                .when()
                .get("/current");
    }
}
