package edu.brown.cs.student.handlers;

import edu.brown.cs.student.server.ErrRequestResponse;
import edu.brown.cs.student.weather.weather;
import spark.Request;
import spark.Response;
import spark.Route;

public class WeatherHandler implements Route {

  weather weatherAPI = new weather();

  /**
   * Method that handles get request and outputs a serialized response.
   *
   * @param request - the request to handle
   * @param response - the response to modify
   * @return A serialized success response or error
   */
  @Override
  public Object handle(Request request, Response response) {
    try {
      String city = request.queryParams("city");
      String state = request.queryParams("state");

      if ((city == null) || (state == null)) {
        return new ErrRequestResponse().serialize();
      }

      return weatherAPI.getWeatherData(city, state);
    } catch (Exception e) {
      // params not properly formatted in request
      return new ErrRequestResponse().serialize();
    }
  }
}
