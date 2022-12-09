package edu.brown.cs.student.closet;
import java.util.*;

/**
 * Class represents the closet for the user. Methods to add and remove items for the closet are in this class.
 */
public class Closet {
    private Map<String, Map<String,String>> clothing;
    private ArrayList validClothesNames;

    public Closet(){
        this.clothing = new HashMap<>();
        this.validClothesNames = new ArrayList<>(Arrays.asList("jeans", "pants", "sweats", "shorts", "skirt",
                "top", "hoodie", "shirt", "long-sleeve", "short-sleeve", "sweater", "tank",
                "jacket", "coat", "hoodie", "shoes", "sneakers", "boots"));
    }

    /**
     * Method adds clothing into the closet. Checks which of the clothing categories it fits into
     * and adds it into the closet respectively
     * @param color
     * @param name
     */
    public void addClothing(String color, String name){
        String clothingName = color+" "+name;
        try {
            if(checkValidity(this.validClothesNames, name)){
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
