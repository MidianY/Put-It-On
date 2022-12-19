package edu.brown.cs.student.RecommenderTest;

import edu.brown.cs.student.handlers.*;
import edu.brown.cs.student.server.UserData;
import edu.brown.cs.student.weather.WeatherAPIUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestRecommenderHandler {
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
        Spark.get("storeGetWeatherData", new StoreGetWeatherHandler(userDB));
        Spark.get("getWeatherData", new GetWeatherHandler(userDB));
        Spark.get("editCloset", new EditClosetHandler(userDB));
        Spark.get("getCloset", new GetClosetHandler(userDB));
        Spark.get("recc", new RecommenderHandler(userDB));

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

    @Test
    public void testSuccessfulRecc() throws IOException, URISyntaxException, InterruptedException {
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

        String mockResp = MockedGetClosetJson.RainSnowResponse;
        Map<String, Object> mockResponseMap = WeatherAPIUtilities.JsonToMap(mockResp);
        assertEquals("success", mockResponseMap.get("result"));
        assertEquals("Portland", mockResponseMap.get("city"));
        assertEquals("OR", mockResponseMap.get("state"));
        assertEquals(34.16, mockResponseMap.get("temp"));
        assertEquals("10n", mockResponseMap.get("icon"));
        assertEquals(0.59, mockResponseMap.get("rain"));
        assertEquals(2.0, mockResponseMap.get("snow"));

        // verify  data was stored into user database
        String URLString2 = "http://localhost:" + Spark.port() + "/" + "getWeatherData";

        // create request
        HttpRequest closetRequest2 = HttpRequest.newBuilder().uri(new URI(URLString2)).GET().build();

        // get response from endpoint
        HttpResponse<String> closetResponse2 =
                client.send(closetRequest2, HttpResponse.BodyHandlers.ofString());

        String resp2 = closetResponse2.body();
        Map<String, Object> responseMap2 = WeatherAPIUtilities.JsonToMap(resp2);
        assertEquals("success", responseMap2.get("result"));
        assertTrue(responseMap2.containsKey("temp"));
        assertTrue(responseMap2.containsKey("feelsLike"));
        assertTrue(responseMap2.containsKey("descr"));
        assertTrue(responseMap2.containsKey("icon"));
        assertTrue(responseMap2.containsKey("rain"));
        assertTrue(responseMap2.containsKey("snow"));
        assertTrue(responseMap2.containsKey("city"));
        assertTrue(responseMap2.containsKey("state"));

        String mockResp2 = MockedGetClosetJson.RainSnowResponse;
        Map<String, Object> mockResponseMap2 = WeatherAPIUtilities.JsonToMap(mockResp2);
        assertEquals("success", mockResponseMap2.get("result"));
        assertEquals("Portland", mockResponseMap2.get("city"));
        assertEquals("OR", mockResponseMap2.get("state"));
        assertEquals(34.16, mockResponseMap2.get("temp"));
        assertEquals("10n", mockResponseMap2.get("icon"));
        assertEquals(0.59, mockResponseMap2.get("rain"));
        assertEquals(2.0, mockResponseMap2.get("snow"));

        String URLString3 =
                "http://localhost:" + Spark.port() + "/" + "editCloset?color=red&item=tank&action=add";

        // create client
        HttpClient client3 = HttpClient.newHttpClient();

        // create request
        HttpRequest closetRequest3 = HttpRequest.newBuilder().uri(new URI(URLString3)).GET().build();

        // get response from endpoint
        HttpResponse<String> closetResponse3 =
                client3.send(closetRequest3, HttpResponse.BodyHandlers.ofString());

        String resp3 = closetResponse3.body();
        Map<String, Object> responseMap3 = WeatherAPIUtilities.JsonToMap(resp3);
        assertEquals("success", responseMap3.get("result"));
        assertEquals("red", responseMap3.get("color"));
        assertEquals("add", responseMap3.get("action"));
        assertEquals("tank", responseMap3.get("item"));

        // verify  data was stored into user database
        String URLString4 = "http://localhost:" + Spark.port() + "/" + "getCloset";

        // create request
        HttpRequest closetRequest4 = HttpRequest.newBuilder().uri(new URI(URLString4)).GET().build();

        // get response from endpoint
        HttpResponse<String> closetResponse4 =
                client.send(closetRequest4, HttpResponse.BodyHandlers.ofString());

        String resp4 = closetResponse4.body();
        Map<String, Object> responseMap4 = WeatherAPIUtilities.JsonToMap(resp4);
        Map<String, Map<String, String>> expectedResponse = new HashMap<>();
        Map<String, String> colorItem = new HashMap<>();
        colorItem.put("color", "red");
        colorItem.put("item", "tank");
        expectedResponse.put("clothing", colorItem);

        // mock the response sent to the frontend
        List<Map<String, String>> clothesList = new ArrayList<>();
        clothesList.addAll(expectedResponse.values());

        assertEquals("success", responseMap4.get("result"));
        assertTrue(responseMap4.containsKey("closet"));
        assertEquals(clothesList, responseMap4.get("closet"));

        String URLString5 = "http://localhost:" + Spark.port() + "/" + "recc";

        HttpRequest closetRequest5 = HttpRequest.newBuilder().uri(new URI(URLString4)).GET().build();

        // get response from endpoint
        HttpResponse<String> closetResponse5 =
                client.send(closetRequest5, HttpResponse.BodyHandlers.ofString());

        String resp5 = closetResponse5.body();
        Map<String, Object> responseMap5 = WeatherAPIUtilities.JsonToMap(resp5);
        Map<String, Map<String, String>> expectedResponse2 = new HashMap<>();
        Map<String, String> colorItem2 = new HashMap<>();
        colorItem2.put("color", "red");
        colorItem2.put("item", "tank");
        expectedResponse2.put("clothing", colorItem);

        // mock the response sent to the frontend
        List<Map<String, String>> clothesList2 = new ArrayList<>();
        clothesList2.addAll(expectedResponse.values());

        assertEquals("success", responseMap4.get("result"));
        assertTrue(responseMap4.containsKey("closet"));
        assertEquals(clothesList, responseMap5.get("closet"));
    }
}
