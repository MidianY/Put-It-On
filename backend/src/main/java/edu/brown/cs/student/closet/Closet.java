package edu.brown.cs.student.closet;
import java.util.*;

/**
 * Class represents the closet for the user. Methods to add and remove items for the closet are in this class.
 */
public class Closet {
    private Map<String, Map<String,String>> clothing;

    public Closet(){
        this.clothing = new HashMap<>();
    }

    /**
     * Method adds clothing into the closet. Checks which of the clothing categories it fits into
     * and adds it into the closet respectively
     * @param color
     * @param name
     */
    public void addClothing(String color, String name){
        ArrayList<String> validBottomNames = new ArrayList<>(Arrays.asList("jeans", "pants", "sweats", "shorts", "skirt"));
        ArrayList<String> validTopNames = new ArrayList<>(Arrays.asList("top", "hoodie", "shirt", "long-sleeve", "short-sleeve", "sweater", "tank"));
        ArrayList<String> validOuterNames = new ArrayList<>(Arrays.asList("jacket", "coat", "hoodie"));
        ArrayList<String> validShoeNames = new ArrayList<>(Arrays.asList("shoes", "sneakers", "boots"));

        String clothingName = color+" "+name;

        try {
            if(checkValidity(validBottomNames, name)){
                Map<String, String> colorItemMap = new HashMap<>();
                colorItemMap.put("item",name);
                colorItemMap.put("color",color);
                this.clothing.put(clothingName, colorItemMap);

            }else if(checkValidity(validTopNames, name)){
                Map<String, String> colorItemMap = new HashMap<>();
                colorItemMap.put("item",name);
                colorItemMap.put("color",color);
                this.clothing.put(clothingName, colorItemMap);

            }else if(checkValidity(validOuterNames, name)){
                Map<String, String> colorItemMap = new HashMap<>();
                colorItemMap.put("item",name);
                colorItemMap.put("color",color);
                this.clothing.put(clothingName, colorItemMap);

            }else if(checkValidity(validShoeNames, name)){
                Map<String, String> colorItemMap = new HashMap<>();
                colorItemMap.put("item",name);
                colorItemMap.put("color",color);
                this.clothing.put(clothingName, colorItemMap);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException();
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

    /**
     * Method removes clothes from the closet
     * @param color
     * @param name
     */
    public void removeClothing(String color, String name){
        String clothingName = color+" "+name;
        for(String clothes: this.clothing.keySet()){
            if(clothingName.equals(clothes)){
                this.clothing.remove(clothingName);
                break;
            }
        }
    }

    /**
     * @return List of dictionaries of clothing data
     */
    public List<Map<String, String>> getClothesData(){
        List<Map<String, String>> clothesList = new ArrayList<>();
        clothesList.addAll(this.clothing.values());
        return clothesList;
    }

}
