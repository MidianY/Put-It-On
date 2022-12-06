package edu.brown.cs.student.weather;

import com.squareup.moshi.JsonEncodingException;
import edu.brown.cs.student.APIKeys.OpenWeather;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

/**
 * weather class that handles implementing the open weather api to fetch different weather data i.e
 * temp, feels like temp, rain, snow, etc.
 */
public class Weather {
  OpenWeather openWeather = new OpenWeather();
  String API_KEY = openWeather.getKey();

  /**
   * Returns weather data (temp, feels like temp, etc)
   *
   * @param city - city name for location
   * @param state - state name for location, abbreviated
   * @return Map with weather information for specified location
   */
  public String getWeatherData(String city, String state)
      throws InterruptedException, URISyntaxException, IOException {
    String urlString =
        "https://api.openweathermap.org/data/2.5/weather?q="
            + city
            + ","
            + state
            + ",US"
            + "&appid="
            + API_KEY
            + "&units=imperial";
    urlString = urlString.replaceAll("\\s", "%20");

    // create client
    HttpClient client = HttpClient.newHttpClient();

    // create request
    HttpRequest weatherRequest = HttpRequest.newBuilder().uri(new URI(urlString)).GET().build();

    // get response from endpoint
    HttpResponse<String> weatherResponse =
        client.send(weatherRequest, HttpResponse.BodyHandlers.ofString());

    if (weatherResponse.statusCode() == 400 || weatherResponse.statusCode() == 404) {
      return new ErrDatasourceResponse().serialize();
    }
    return getWeatherDataFromJson(weatherResponse.body(), city, state);
  }

  /**
   * Uses response map to return a serialized map with relevant weather data included
   *
   * @param json - json that open weather api provides
   * @return serialized map with relevant weather data
   */
  public String getWeatherDataFromJson(String json, String city, String state) throws IOException {
    try {
      Map<String, Object> responseMap = WeatherAPIUtilities.JsonToMap(json);
      Map<String, Double> mainMap = (Map<String, Double>) responseMap.get("main");
      ArrayList<Map<String, Object>> weatherMapList =
          (ArrayList<Map<String, Object>>) responseMap.get("weather");
      Map<String, Object> weatherMap = weatherMapList.get(0);

      Double rain = 0.0;
      Double snow = 0.0;
      if (responseMap.get("rain") != null) {
        Map<String, Double> rainMap = (Map<String, Double>) responseMap.get("rain");
        rain = rainMap.get("1h");
      }

      if (responseMap.get("snow") != null) {
        Map<String, Double> snowMap = (Map<String, Double>) responseMap.get("snow");
        snow = snowMap.get("1h");
      }

      return new WeatherSuccessResponse(
              city,
              state,
              mainMap.get("temp"),
              mainMap.get("feels_like"),
              (String) weatherMap.get("description"),
              (String) weatherMap.get("icon"),
              rain,
              snow)
          .serialize();
    } catch (JsonEncodingException e) {
      return new ErrDatasourceResponse().serialize();
    }
  }

  public static void main(String[] args)
      throws IOException, URISyntaxException, InterruptedException {
    Weather weatherObj = new Weather();
    System.out.println(weatherObj.getWeatherData("Los Angeles", "CA"));
  }
}
