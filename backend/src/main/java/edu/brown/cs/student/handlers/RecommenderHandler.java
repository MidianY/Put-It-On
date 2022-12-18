package edu.brown.cs.student.handlers;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.server.UserData;
import recommender.Recommender;
import spark.Request;
import spark.Response;
import spark.Route;
import java.util.*;

public class RecommenderHandler implements Route {

    private final UserData data;
    private final Recommender algorithm;

    public RecommenderHandler(UserData data){

        algorithm = new Recommender(data);
        this.data = data;

    }

    @Override
    public Object handle(Request request, Response response) {

        try {
            return new GetSuccessResponse(algorithm.recommender(this.data)).serialize();
        }
        catch (Exception e){
            e.printStackTrace();
            return new GetFailureResponse().serialize();
        }

    }

    public record GetSuccessResponse(List<Map<String,String>> data) {
        /**
         * @return this response, serialized as Json
         */
        String serialize() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "success");
            result.put("outfit", data);
            Moshi moshi = new Moshi.Builder().build();
            return moshi.adapter(Map.class).toJson(result);
        }
    }

    public record GetFailureResponse() {
        /**
         * @return this response, serialized as Json
         */
        String serialize() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "bad_json_error");
            result.put("further description", "load weather");
            Moshi moshi = new Moshi.Builder().build();
            return moshi.adapter(Map.class).toJson(result);
        }
    }

}