package edu.brown.cs.student.WeatherTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.brown.cs.student.handlers.GetWeatherHandler;
import edu.brown.cs.student.handlers.StoreGetWeatherHandler;
import edu.brown.cs.student.recommender.RecommenderUtilities;
import edu.brown.cs.student.server.UserData;
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
public class TestWeatherHandlers {

  @BeforeAll
  public static void spark_port_setup() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING);
  }

  @BeforeEach
  public void setup() {
    // Re-initialize state, etc. for _every_ test method run

    UserData userDB = new UserData();

    // In fact, restart the entire Spark server for every test!
    Spark.get("/storeGetWeatherData", new StoreGetWeatherHandler(userDB));
    Spark.get("/getWeatherData", new GetWeatherHandler(userDB));

    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  @AfterEach
  public void teardown() {
    // Gracefully stop Spark listening on endpoints
    Spark.unmap("/storeGetWeatherData");
    Spark.unmap("/getWeatherData");

    Spark.awaitStop(); // don't proceed until the server is stopped
  }

  /**
   * Tests that valid query returns expected map with all relevant weather data components
   */
  @Test
  public void testValidWeather() throws IOException, URISyntaxException, InterruptedException {
    String URLString =
            "http://localhost:" + Spark.port() + "/" + "storeGetWeatherData?city=Portland&state=OR";

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
    assertTrue(responseMap.containsKey("city"));
    assertTrue(responseMap.containsKey("state"));

    // verify weather data was stored into user database
    String URLString2 = "http://localhost:" + Spark.port() + "/" + "getWeatherData";

    // create request
    HttpRequest weatherRequest2 = HttpRequest.newBuilder().uri(new URI(URLString2)).GET().build();

    // get response from endpoint
    HttpResponse<String> weatherResponse2 =
            client.send(weatherRequest2, HttpResponse.BodyHandlers.ofString());
    String resp2 = weatherResponse2.body();
    assertEquals(resp, resp2);
  }

  /**
   * Tests for when the location given is invalid i.e doesn't exist, or weather api can't get info
   * for some reason
   */
  @Test
  public void testRecommender() throws URISyntaxException, IOException, InterruptedException {
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

    // verify that user database isn't updated
    String URLString2 = "http://localhost:" + Spark.port() + "/" + "getWeatherData";

    // create request
    HttpRequest weatherRequest2 = HttpRequest.newBuilder().uri(new URI(URLString2)).GET().build();

    // get response from endpoint
    HttpResponse<String> weatherResponse2 =
            client.send(weatherRequest2, HttpResponse.BodyHandlers.ofString());
    String resp2 = weatherResponse2.body();
    assertEquals(resp, resp2);

    String URLString6 = "http://localhost:" + Spark.port() + "/" + "editCloset?color=red&item=tank&action=add";

    // create client
    HttpClient client6 = HttpClient.newHttpClient();

    // create request
    HttpRequest closetRequest = HttpRequest.newBuilder().uri(new URI(URLString6)).GET().build();

    // get response from endpoint
    HttpResponse<String> closetResponse =
            client6.send(closetRequest, HttpResponse.BodyHandlers.ofString());

    String resp6 = closetResponse.body();
    Map<String, Object> responseMap6 = WeatherAPIUtilities.JsonToMap(resp6);
    assertEquals("success", responseMap6.get("result"));
    assertEquals("red", responseMap6.get("color"));
    assertEquals("add", responseMap6.get("action"));
    assertEquals("tank", responseMap6.get("item"));

    // verify  data was stored into user database
    String URLString7 = "http://localhost:" + Spark.port() + "/" + "getCloset";

    // create request
    HttpRequest closetRequest7 = HttpRequest.newBuilder().uri(new URI(URLString7)).GET().build();

    // get response from endpoint
    HttpResponse<String> closetResponse7 =
            client.send(closetRequest7, HttpResponse.BodyHandlers.ofString());

    String resp7 = closetResponse7.body();
    Map<String, Object> responseMap7 = RecommenderUtilities.JsonToMap(resp7);

    assertEquals("success", responseMap7.get("result"));
    assertEquals(4, responseMap7.size());
  }
}
