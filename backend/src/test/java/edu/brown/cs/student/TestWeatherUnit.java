package edu.brown.cs.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.brown.cs.student.weather.ErrDatasourceResponse;
import edu.brown.cs.student.weather.WeatherAPIUtilities;
import edu.brown.cs.student.weather.weather;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import org.junit.jupiter.api.Test;

/** Unit tests for handling of weather API */
public class TestWeatherUnit {

  weather weatherAPI = new weather();

  /** Tests that valid query returns expected map with all relevant weather data components */
  @Test
  public void testValidLocation() throws URISyntaxException, IOException, InterruptedException {
    String responseJson = weatherAPI.getWeatherData("Los Angeles", "CA");
    Map<String, Object> responseMap = WeatherAPIUtilities.JsonToMap(responseJson);
    assertEquals("success", responseMap.get("result"));
    assertTrue(responseMap.containsKey("temp"));
    assertTrue(responseMap.containsKey("feelsLike"));
    assertTrue(responseMap.containsKey("descr"));
    assertTrue(responseMap.containsKey("icon"));
    assertTrue(responseMap.containsKey("rain"));
    assertTrue(responseMap.containsKey("snow"));
    assertTrue(responseMap.containsKey("city"));
    assertTrue(responseMap.containsKey("state"));
  }

  /**
   * Tests for when the location given is invalid i.e doesn't exist, or weather api can't get info
   * for some reason
   */
  @Test
  public void testInvalidLocation() throws URISyntaxException, IOException, InterruptedException {
    String responseJson = weatherAPI.getWeatherData("Kaka Land", "OR");
    String expectedJson = new ErrDatasourceResponse().serialize();
    assertEquals(expectedJson, responseJson);

    String responseJson2 = weatherAPI.getWeatherData("03ok", "CC");
    assertEquals(expectedJson, responseJson2);
  }

  /**
   * Test mocked api responses to determine if method is accurately filtering info
   *
   * @throws IOException
   */
  @Test
  public void mockTestMapResponse() throws IOException {
    String rainSnowJson = MockedWeatherJsons.RainSnow;
    String expectedJson = MockedWeatherJsons.RainSnowResponse;
    assertEquals(expectedJson, weatherAPI.getWeatherDataFromJson(rainSnowJson, "Portland", "OR"));

    String noRainSnowJson = MockedWeatherJsons.noRainSnow;
    String expectedJson2 = MockedWeatherJsons.noRainSnowResponse;
    assertEquals(
        expectedJson2, weatherAPI.getWeatherDataFromJson(noRainSnowJson, "Portland", "OR"));
    assertEquals(
        new ErrDatasourceResponse().serialize(),
        weatherAPI.getWeatherDataFromJson(MockedWeatherJsons.InValidResponse, "Portland", "OR"));
  }
}
