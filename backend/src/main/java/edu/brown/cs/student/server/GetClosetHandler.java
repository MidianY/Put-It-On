package edu.brown.cs.student.server;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.outfit.Closet;
import edu.brown.cs.student.server.errorRepsonses.DataError;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetClosetHandler implements Route {
    private Closet closet;

    public GetClosetHandler(Closet closet){
        this.closet = closet;
    }

    //show color and item in closet
    @Override
    public Object handle(Request request, Response response) throws Exception {
        try{
            if (this.closet.getClothing()==null){
                return new DataError().serialize();
            } else{
                return new GetSuccessResponse(this.closet.getClothing()).serialize();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public record GetSuccessResponse(List<String> data) {
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
