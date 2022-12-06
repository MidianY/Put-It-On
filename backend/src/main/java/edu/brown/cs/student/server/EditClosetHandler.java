package edu.brown.cs.student.server;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.outfit.Closet;
import edu.brown.cs.student.server.errorRepsonses.BadClothingError;
import edu.brown.cs.student.server.errorRepsonses.BadRequestError;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import java.util.*;

public class EditClosetHandler implements Route {
    private Closet closet;

    public EditClosetHandler(Closet closet) {
        this.closet = closet;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        ArrayList<String> validClothesNames = new ArrayList<>(Arrays.asList("jeans", "pants", "sweats", "shorts", "skirt",
                "top", "hoodie", "shirt", "long-sleeve", "short-sleeve", "sweater", "tank",
                "jacket", "coat", "hoodie", "shoes", "sneakers", "boots"));

        QueryParamsMap qm = request.queryMap();
        String color = qm.value("color");
        String clothingItem = qm.value("item");
        String action = qm.value("action");

        try {
            if (!qm.hasKey("item") || !qm.hasKey("color") || !qm.hasKey("action")) {
                return new BadRequestError().serialize();
            }
            if(checkValidity(validClothesNames, clothingItem) && checkValidityColor(color)){
                if(action.equals("add")) {
                    this.closet.addClothing(color, clothingItem);
                    return new LoadSuccessResponse(color, clothingItem, action).serialize();
                }else if (action.equals("remove")){
                    this.closet.removeClothing(color, clothingItem);
                    return new LoadSuccessResponse(color, clothingItem, action).serialize();
                }
            }
            return new BadClothingError().serialize();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public record LoadSuccessResponse(String color, String item, String action) {

        /**
         * @return this response, serialized as Json
         */
        String serialize() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "success");
            result.put("color", color);
            result.put("clothes", item);
            result.put("action", action);
            Moshi moshi = new Moshi.Builder().build();
            return moshi.adapter(Map.class).toJson(result);
        }
    }

    /**
     * Method used to validate whether the item of clothing the user typed is a valid clothing
     * item to add into the closet
     * @param clothes valid names for clothing items
     * @param name name the user types
     * @return
     */
    private boolean checkValidity(List clothes, String name){
        if (clothes.contains(name)){
            return true;
        } return false;
    }

    private boolean checkValidityColor(String name){
        ArrayList<String> colorName = new ArrayList<>(Arrays.asList("black", "blue", "yellow",
                "green", "orange", "purple", "indigo", "violet", "red"));
        if (colorName.contains(name)){
            return true;
        } return false;
    }
}
