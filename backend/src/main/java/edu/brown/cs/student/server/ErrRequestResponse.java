package edu.brown.cs.student.server;

import com.squareup.moshi.Moshi;

/**
 * Response object to return if the given data source wasn't accessible (e.g., the file didn't exist
 * or the Open Weather API returned an error for a given location).
 *
 * @param result - string indicating datasource error
 */
public record ErrRequestResponse(String result) {
  public ErrRequestResponse() {
    this("API request incorrectly formatted");
  }

  /**
   * @return this response, serialized as Json
   */
  public String serialize() {
    Moshi moshi = new Moshi.Builder().build();
    return moshi.adapter(ErrRequestResponse.class).toJson(this);
  }
}
