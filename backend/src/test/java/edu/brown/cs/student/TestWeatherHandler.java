package edu.brown.cs.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.brown.cs.student.handlers.WeatherHandler;
import edu.brown.cs.student.server.ErrRequestResponse;
import edu.brown.cs.student.weather.ErrDatasourceResponse;
import edu.brown.cs.student.weather.WeatherAPIUtilities;
import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

/** Tests weather handler works as expected */
public class TestWeatherHandler {

  @BeforeAll
  public static void spark_port_setup() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING);
  }

  @BeforeEach
  public void setup() {
    // Re-initialize state, etc. for _every_ test method run

    // In fact, restart the entire Spark server for every test!
    Spark.get("/getWeatherData", new WeatherHandler());
    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  @AfterEach
  public void teardown() {
    // Gracefully stop Spark listening on endpoints
    Spark.unmap("/getWeatherData");
    Spark.awaitStop(); // don't proceed until the server is stopped
  }

  /** Tests that valid query returns expected map with all relevant weather data components */
  @Test
  public void testValidWeather() throws IOException, URISyntaxException, InterruptedException {
    String URLString =
        "http://localhost:" + Spark.port() + "/" + "getWeatherData?city=Portland&state=OR";

    // create client
    HttpClient client = HttpClient.newHttpClient();

    // create request
    HttpRequest weatherRequest = HttpRequest.newBuilder().uri(new URI(URLString)).GET().build();

    // get response from endpoint
    HttpResponse<String> weatherResponse =
        client.send(weatherRequest, HttpResponse.BodyHandlers.ofString());
    String resp = weatherResponse.body();

    Map<String, Object> responseMap = WeatherAPIUtilities.JsonToMap(resp);
    assertEquals("success", responseMap.get("result"));
    assertTrue(responseMap.containsKey("temp"));
    assertTrue(responseMap.containsKey("feelsLike"));
    assertTrue(responseMap.containsKey("descr"));
    assertTrue(responseMap.containsKey("icon"));
    assertTrue(responseMap.containsKey("rain"));
    assertTrue(responseMap.containsKey("snow"));
  }

  /**
   * Tests for when the location given is invalid i.e doesn't exist, or weather api can't get info
   * for some reason
   */
  @Test
  public void testInvalidLocation() throws URISyntaxException, IOException, InterruptedException {
    String URLString =
        "http://localhost:" + Spark.port() + "/" + "getWeatherData?city=Mario123&state=OR";

    // create client
    HttpClient client = HttpClient.newHttpClient();

    // create request
    HttpRequest weatherRequest = HttpRequest.newBuilder().uri(new URI(URLString)).GET().build();

    // get response from endpoint
    HttpResponse<String> weatherResponse =
        client.send(weatherRequest, HttpResponse.BodyHandlers.ofString());
    String resp = weatherResponse.body();
    String expectedJson = new ErrDatasourceResponse().serialize();
    assertEquals(expectedJson, resp);
  }

  @Test
  public void testInvalidQuery() throws URISyntaxException, IOException, InterruptedException {
    String URLString = "http://localhost:" + Spark.port() + "/" + "getWeatherData?city=";

    // create client
    HttpClient client = HttpClient.newHttpClient();

    // create request
    HttpRequest weatherRequest = HttpRequest.newBuilder().uri(new URI(URLString)).GET().build();

    // get response from endpoint
    HttpResponse<String> weatherResponse =
        client.send(weatherRequest, HttpResponse.BodyHandlers.ofString());
    String resp = weatherResponse.body();
    String expectedJson = new ErrRequestResponse().serialize();
    assertEquals(expectedJson, resp);
  }
}
