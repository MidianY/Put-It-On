package edu.brown.cs.student.handlers;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.server.UserData;
import edu.brown.cs.student.server.errorRepsonses.BadClothingError;
import edu.brown.cs.student.server.errorRepsonses.BadRequestError;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import java.util.*;

/**
 * Class that processes the editCloset endpoint request. This class accesses
 * closet data from the user database shared state variable and edits the closet depending
 * on whether it is an add or remove request
 */
public class EditClosetHandler implements Route {
    private UserData db;
    private ArrayList validClothesNames;


    public EditClosetHandler(UserData db) {
        this.db = db;
        this.validClothesNames = new ArrayList<>(Arrays.asList("jeans", "pants", "sweats", "shorts", "skirt",
                "top", "hoodie", "shirt", "long-sleeve", "short-sleeve", "sweater", "tank",
                "jacket", "coat", "hoodie", "shoes", "sneakers", "boots"));
    }

    /**
     * Method handles adding and removing clothing items from the closet
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        QueryParamsMap qm = request.queryMap();
        String color = qm.value("color");
        String clothingItem = qm.value("item");
        String action = qm.value("action");

        try {
            if (!qm.hasKey("item") || !qm.hasKey("color") || !qm.hasKey("action")) {
                return new BadRequestError().serialize();
            }
            if(checkValidity(this.validClothesNames, clothingItem)){
                if(action.equals("add")) {
                    this.db.getCurrentCloset().addClothing(color, clothingItem);
                    return new EditSuccessResponse(color, clothingItem, action).serialize();
                }else if (action.equals("remove")){
                    this.db.getCurrentCloset().removeClothing(color, clothingItem);
                    return new EditSuccessResponse(color, clothingItem, action).serialize();
                }
            }
            return new BadClothingError().serialize();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Success response if the item can be added/removed from the closet
     */
    public record EditSuccessResponse(String color, String item, String action) {
        /**
         * @return this response, serialized as Json
         */
        String serialize() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "success");
            result.put("color", color);
            result.put("item", item);
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
}
