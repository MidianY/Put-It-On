package edu.brown.cs.student.handlers;

import edu.brown.cs.student.server.ErrRequestResponse;
import edu.brown.cs.student.server.UserData;
import edu.brown.cs.student.weather.Weather;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Class that processes the storeGetWeatherData endpoint request. This class both stores the
 * weather/city data into the database shared state variable and returns its contents to the
 * endpoint caller
 */
public class StoreGetWeatherHandler implements Route {

  private UserData db;

  public StoreGetWeatherHandler(UserData db) {
    this.db = db;
  }

  Weather weatherAPI = new Weather();

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

      String resp = weatherAPI.getWeatherData(city, state);

      // only store if query was valid
      if (resp.contains("success")) {
        db.setLocationData(resp);
      }

      return resp;
    } catch (Exception e) {
      // params not properly formatted in request
      return new ErrRequestResponse().serialize();
    }
  }
}
