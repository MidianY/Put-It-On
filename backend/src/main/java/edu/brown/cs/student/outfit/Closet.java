package edu.brown.cs.student.outfit;
import java.util.*;

/**
 * Class represents the closet for the user. Methods to add and remove items for the closet are in this class.
 */
public class Closet {
    private Map<String, ItemKind> clothesMap;

    public Closet(){
        this.clothesMap = new HashMap<>();
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
                ClothingItem bottom = new ClothingItem(clothingName, ItemKind.BOTTOM);
                this.clothesMap.put(bottom.getName(), bottom.getItemKind());

            }else if(checkValidity(validTopNames, name)){
                ClothingItem top = new ClothingItem(clothingName, ItemKind.TOP);
                this.clothesMap.put(top.getName(), top.getItemKind());

            }else if(checkValidity(validOuterNames, name)){
                ClothingItem outer = new ClothingItem(clothingName, ItemKind.OUTER);
                this.clothesMap.put(outer.getName(), outer.getItemKind());

            }else if(checkValidity(validShoeNames, name)){
                ClothingItem shoes = new ClothingItem(clothingName, ItemKind.SHOES);
                this.clothesMap.put(shoes.getName(), shoes.getItemKind());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public void removeClothing(String color, String name){
        String clothingName = color+" "+name;
        for(String clothes: this.clothesMap.keySet()){
            if(clothingName.equals(clothes)){
                this.clothesMap.remove(clothingName);
                break;
            }
        }
    }

    public void formatList(){

    }

    //returns dictionary of clothing item mapped to its type
    public Map<String, ItemKind> getClothesMap(){
        return this.clothesMap;
    }

    public List<String> getClothing(){
        List<String> clothesList = new ArrayList<>();
        clothesList.addAll(this.clothesMap.keySet());
        return clothesList;
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
