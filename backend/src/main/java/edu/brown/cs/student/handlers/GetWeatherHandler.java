package edu.brown.cs.student.handlers;

import edu.brown.cs.student.server.UserData;
import edu.brown.cs.student.weather.ErrDatasourceResponse;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Class that processes the getWeatherData endpoint request. This class returns the contents of the
 * weather/city data in the shared state user database to the endpoint caller
 */
public class GetWeatherHandler implements Route {

  private UserData db;

  public GetWeatherHandler(UserData db) {
    this.db = db;
  }

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
      if (this.db.getLocationData() == null) {
        return new ErrDatasourceResponse().serialize();
      }
      return this.db.getLocationData();
    } catch (Exception e) {
      System.out.println("The following exception occurred: " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }
}
