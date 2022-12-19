package edu.brown.cs.student.ClosetTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.brown.cs.student.handlers.EditClosetHandler;
import edu.brown.cs.student.handlers.GetClosetHandler;
import edu.brown.cs.student.server.UserData;
import edu.brown.cs.student.server.errorRepsonses.BadClothingError;
import edu.brown.cs.student.server.errorRepsonses.BadRequestError;
import edu.brown.cs.student.weather.WeatherAPIUtilities;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class TestClosetHandlers {

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
    Spark.get("/editCloset", new EditClosetHandler(userDB));
    Spark.get("/getCloset", new GetClosetHandler(userDB));

    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  @AfterEach
  public void teardown() {
    // Gracefully stop Spark listening on endpoints
    Spark.unmap("/editCloset");
    Spark.unmap("/getCloset");

    Spark.awaitStop(); // don't proceed until the server is stopped
  }

  /** Tests that valid query returns expected map with all relevant weather data components */
  @Test
  public void testValidCloset() throws IOException, URISyntaxException, InterruptedException {
    String URLString =
            "http://localhost:" + Spark.port() + "/" + "editCloset?color=red&item=tank&action=add";

    // create client
    HttpClient client = HttpClient.newHttpClient();

    // create request
    HttpRequest closetRequest = HttpRequest.newBuilder().uri(new URI(URLString)).GET().build();

    // get response from endpoint
    HttpResponse<String> closetResponse =
            client.send(closetRequest, HttpResponse.BodyHandlers.ofString());

    String resp = closetResponse.body();
    Map<String, Object> responseMap = WeatherAPIUtilities.JsonToMap(resp);
    assertEquals("success", responseMap.get("result"));
    assertEquals("red", responseMap.get("color"));
    assertEquals("add", responseMap.get("action"));
    assertEquals("tank", responseMap.get("item"));

    // verify  data was stored into user database
    String URLString2 = "http://localhost:" + Spark.port() + "/" + "getCloset";

    // create request
    HttpRequest closetRequest2 = HttpRequest.newBuilder().uri(new URI(URLString2)).GET().build();

    // get response from endpoint
    HttpResponse<String> closetResponse2 =
            client.send(closetRequest2, HttpResponse.BodyHandlers.ofString());

    String resp2 = closetResponse2.body();
    Map<String, Object> responseMap2 = WeatherAPIUtilities.JsonToMap(resp2);
    Map<String, Map<String, String>> expectedResponse = new HashMap<>();
    Map<String, String> colorItem = new HashMap<>();
    colorItem.put("color", "red");
    colorItem.put("item", "tank");
    expectedResponse.put("clothing", colorItem);

    // mock the response sent to the frontend
    List<Map<String, String>> clothesList = new ArrayList<>();
    clothesList.addAll(expectedResponse.values());

    assertEquals("success", responseMap2.get("result"));
    assertTrue(responseMap2.containsKey("closet"));
    assertEquals(clothesList, responseMap2.get("closet"));
  }

  /**
   * Tests for when the clothes given is invalid i.e the clothing item doesn't exist within our
   * closet
   */
  @Test
  public void testInvalidCloset() throws URISyntaxException, IOException, InterruptedException {
    String URLString =
            "http://localhost:" + Spark.port() + "/" + "editCloset?color=red&item=tk&action=add";

    // create client
    HttpClient client = HttpClient.newHttpClient();

    // create request
    HttpRequest closetRequest = HttpRequest.newBuilder().uri(new URI(URLString)).GET().build();

    // get response from endpoint
    HttpResponse<String> closetResponse =
            client.send(closetRequest, HttpResponse.BodyHandlers.ofString());

    String resp = closetResponse.body();
    String expectedJson = new BadClothingError().serialize();
    assertEquals(expectedJson, resp);

    // verify that user database isn't updated and closet is empty
    String URLString2 = "http://localhost:" + Spark.port() + "/" + "getCloset";

    // create request
    HttpRequest closetRequest2 = HttpRequest.newBuilder().uri(new URI(URLString2)).GET().build();

    // get response from endpoint
    HttpResponse<String> closetResponse2 =
            client.send(closetRequest2, HttpResponse.BodyHandlers.ofString());
    String resp2 = closetResponse2.body();
    Map<String, Object> responseMap2 = WeatherAPIUtilities.JsonToMap(resp2);
    assertEquals(new ArrayList<>(), responseMap2.get("closet"));
  }

  /**
   * Tests for when the clothes given is invalid i.e the clothing item doesn't exist within our
   * closet
   */
  @Test
  public void testInvalidQuery() throws URISyntaxException, IOException, InterruptedException {
    String URLString =
            "http://localhost:" + Spark.port() + "/" + "editCloset?c=red&iem=tank&actin=add";

    // create client
    HttpClient client = HttpClient.newHttpClient();

    // create request
    HttpRequest closetRequest = HttpRequest.newBuilder().uri(new URI(URLString)).GET().build();

    // get response from endpoint
    HttpResponse<String> closetResponse =
            client.send(closetRequest, HttpResponse.BodyHandlers.ofString());

    String resp = closetResponse.body();
    String expectedJson = new BadRequestError().serialize();
    assertEquals(expectedJson, resp);

    // verify that user database isn't updated and closet is empty
    String URLString2 = "http://localhost:" + Spark.port() + "/" + "getCloset";

    // create request
    HttpRequest closetRequest2 = HttpRequest.newBuilder().uri(new URI(URLString2)).GET().build();

    // get response from endpoint
    HttpResponse<String> closetResponse2 =
            client.send(closetRequest2, HttpResponse.BodyHandlers.ofString());
    String resp2 = closetResponse2.body();
    Map<String, Object> responseMap2 = WeatherAPIUtilities.JsonToMap(resp2);
    assertEquals(new ArrayList<>(), responseMap2.get("closet"));
  }
}
