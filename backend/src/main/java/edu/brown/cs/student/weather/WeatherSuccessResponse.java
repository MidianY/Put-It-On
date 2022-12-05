package edu.brown.cs.student.weather;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

/** Response object to send, containing success notification and csv content */
public record WeatherSuccessResponse(
    String result,
    Double temp,
    Double feelsLike,
    String descr,
    String icon,
    Double rain,
    Double snow) {

  public WeatherSuccessResponse(
      Double temp, Double feelsLike, String descr, String icon, Double rain, Double snow) {
    this("success", temp, feelsLike, descr, icon, rain, snow);
  }

  /**
   * @return this response, serialized as Json
   */
  String serialize() {
    try {
      Moshi moshi = new Moshi.Builder().build();

      JsonAdapter<WeatherSuccessResponse> adapter = moshi.adapter(WeatherSuccessResponse.class);
      return adapter.toJson(this);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
}
