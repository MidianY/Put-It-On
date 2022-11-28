package edu.brown.cs.student.outfit;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class represents the closet for the user. Methods to add and remove items for the closet are in this class.
 */
public class Closet {
    private final Map<String, ClothingItem> clothingItems;
    // private final List<String> clothingItems = new ArrayList<>();

    public Closet(){
        this.clothingItems = new HashMap<>();
    }

    public void addBottom(String name){
        ClothingItem bottom = new ClothingItem(name, ItemKind.BOTTOM);
        this.clothingItems.put(name, bottom);
    }

    public Set<String> getClothes(){
        return this.clothingItems.keySet();
    }



}
