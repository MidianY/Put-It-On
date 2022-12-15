package edu.brown.cs.student.handlers;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.server.UserData;
import edu.brown.cs.student.server.errorRepsonses.DataError;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

public class GetClosetStatsHandler implements Route {
    private UserData db;

    public GetClosetStatsHandler(UserData db){
        this.db = db;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            if (this.db.getCurrentCloset().getClothesData() == null) {
                return new DataError().serialize();
            } else {
                return new GetSuccessResponse(this.db.getCurrentCloset().getStats()).serialize();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /** Successful response if the closet data can be returned */
    public record GetSuccessResponse(Map<String, Integer> data) {
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
