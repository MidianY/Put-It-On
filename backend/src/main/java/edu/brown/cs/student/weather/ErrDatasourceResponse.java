package edu.brown.cs.student.weather;

import com.squareup.moshi.Moshi;

/**
 * Response object to return if the given data source wasn't accessible (e.g., the file didn't exist
 * or the Open Weather API returned an error for a given location or no city is currently stored
 * (i.e it is null).
 *
 * @param result - string indicating datasource error
 */
public record ErrDatasourceResponse(String result) {
  public ErrDatasourceResponse() {
    this("city not found");
  }

  /**
   * @return this response, serialized as Json
   */
  public String serialize() {
    Moshi moshi = new Moshi.Builder().build();
    return moshi.adapter(ErrDatasourceResponse.class).toJson(this);
  }
}
