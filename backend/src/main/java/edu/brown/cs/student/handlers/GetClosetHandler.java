package edu.brown.cs.student.handlers;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.server.UserData;
import edu.brown.cs.student.server.errorRepsonses.DataError;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Class that processes the getCloset endpoint request. This class stores the closet data into the
 * user database shared state variable and returns its contents to the endpoint caller
 */
public class GetClosetHandler implements Route {
  private UserData db;

  public GetClosetHandler(UserData db) {
    this.db = db;
  }

  /**
   * method returns the closet data when the user makes a getCloset request and returns a serialized
   * response
   *
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    try {
      if (this.db.getCurrentCloset().getClothesData() == null) {
        return new DataError().serialize();
      } else {
        return new GetSuccessResponse(this.db.getCurrentCloset().getClothesData()).serialize();
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  /** Successful response if the closet data can be returned */
  public record GetSuccessResponse(List<Map<String, String>> data) {
    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      HashMap<String, Object> result = new HashMap<>();
      result.put("result", "success");
      result.put("closet", data);
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(Map.class).toJson(result);
    }
  }
}
