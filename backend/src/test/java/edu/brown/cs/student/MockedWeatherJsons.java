package edu.brown.cs.student;

public class MockedWeatherJsons {

  public static String RainSnow =
      "{\n"
          + "\t\"coord\": {\n"
          + "\t\t\"lon\": -122.6762,\n"
          + "\t\t\"lat\": 45.5234\n"
          + "\t},\n"
          + "\t\"weather\": [{\n"
          + "\t\t\"id\": 500,\n"
          + "\t\t\"main\": \"Rain\",\n"
          + "\t\t\"description\": \"light rain\",\n"
          + "\t\t\"icon\": \"10n\"\n"
          + "\t}],\n"
          + "\t\"base\": \"stations\",\n"
          + "\t\"main\": {\n"
          + "\t\t\"temp\": 34.16,\n"
          + "\t\t\"feels_like\": 27.46,\n"
          + "\t\t\"temp_min\": 31.96,\n"
          + "\t\t\"temp_max\": 36.48,\n"
          + "\t\t\"pressure\": 1015,\n"
          + "\t\t\"humidity\": 92\n"
          + "\t},\n"
          + "\t\"visibility\": 10000,\n"
          + "\t\"wind\": {\n"
          + "\t\t\"speed\": 8.01,\n"
          + "\t\t\"deg\": 42,\n"
          + "\t\t\"gust\": 13\n"
          + "\t},\n"
          + "\t\"rain\": {\n"
          + "\t\t\"1h\": 0.59\n"
          + "\t},\n"
          + "\t\"snow\": {\n"
          + "\t\t\"1h\": 2\n"
          + "\t},\n"
          + "\t\"clouds\": {\n"
          + "\t\t\"all\": 100\n"
          + "\t},\n"
          + "\t\"dt\": 1670206638,\n"
          + "\t\"sys\": {\n"
          + "\t\t\"type\": 2,\n"
          + "\t\t\"id\": 2013569,\n"
          + "\t\t\"country\": \"US\",\n"
          + "\t\t\"sunrise\": 1670168051,\n"
          + "\t\t\"sunset\": 1670200089\n"
          + "\t},\n"
          + "\t\"timezone\": -28800,\n"
          + "\t\"id\": 5746545,\n"
          + "\t\"name\": \"Portland\",\n"
          + "\t\"cod\": 200\n"
          + "}";

  public static String noRainSnow =
      "{\n"
          + "\t\"coord\": {\n"
          + "\t\t\"lon\": -122.6762,\n"
          + "\t\t\"lat\": 45.5234\n"
          + "\t},\n"
          + "\t\"weather\": [{\n"
          + "\t\t\"id\": 500,\n"
          + "\t\t\"main\": \"Rain\",\n"
          + "\t\t\"description\": \"light rain\",\n"
          + "\t\t\"icon\": \"10n\"\n"
          + "\t}],\n"
          + "\t\"base\": \"stations\",\n"
          + "\t\"main\": {\n"
          + "\t\t\"temp\": 34.16,\n"
          + "\t\t\"feels_like\": 27.46,\n"
          + "\t\t\"temp_min\": 31.96,\n"
          + "\t\t\"temp_max\": 36.48,\n"
          + "\t\t\"pressure\": 1015,\n"
          + "\t\t\"humidity\": 92\n"
          + "\t},\n"
          + "\t\"visibility\": 10000,\n"
          + "\t\"wind\": {\n"
          + "\t\t\"speed\": 8.01,\n"
          + "\t\t\"deg\": 42,\n"
          + "\t\t\"gust\": 13\n"
          + "\t},\n"
          + "\t\"clouds\": {\n"
          + "\t\t\"all\": 100\n"
          + "\t},\n"
          + "\t\"dt\": 1670206638,\n"
          + "\t\"sys\": {\n"
          + "\t\t\"type\": 2,\n"
          + "\t\t\"id\": 2013569,\n"
          + "\t\t\"country\": \"US\",\n"
          + "\t\t\"sunrise\": 1670168051,\n"
          + "\t\t\"sunset\": 1670200089\n"
          + "\t},\n"
          + "\t\"timezone\": -28800,\n"
          + "\t\"id\": 5746545,\n"
          + "\t\"name\": \"Portland\",\n"
          + "\t\"cod\": 200\n"
          + "}";

  public static String RainSnowResponse =
      "{\"result\":\"success\",\"temp\":34.16,"
          + "\"feelsLike\":27.46,\"descr\":\"light rain\",\"icon\":\"10n\",\"rain\":0.59,"
          + "\"snow\":2.0}";

  public static String noRainSnowResponse =
      "{\"result\":\"success\",\"temp\":34.16,"
          + "\"feelsLike\":27.46,\"descr\":\"light rain\",\"icon\":\"10n\",\"rain\":0.0,\"snow\":0.0}";

  public static String InValidResponse = "invalid";
}
