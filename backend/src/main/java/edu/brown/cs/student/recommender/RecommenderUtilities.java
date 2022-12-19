package edu.brown.cs.student.recommender;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RecommenderUtilities {

    /**
     * Converts Json to a Java Map Data Type
     *
     * @param json
     * @return List of Map with key and value as strings representing output from direct geocoding API
     *     call
     * @throws IOException when json can't be converted to java map
     */
    public static Map<String, Object> JsonToMap(String json) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Map<String, Object>> jsonAdapter =
                moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class, List.class));
        return jsonAdapter.fromJson(json);
    }

    /**
     * Method that deserializes a json string into a java map
     *
     * @throws IOException
     */
    public static Object deserializeJson(String json) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter jsonAdapter = moshi.adapter(Map.class);
        return jsonAdapter.fromJson(json);
    }
}
