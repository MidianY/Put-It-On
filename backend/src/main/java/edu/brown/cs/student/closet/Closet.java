package edu.brown.cs.student.closet;

import java.util.*;

/**
 * Class represents the closet for the user. Methods to add and remove items for the closet are in
 * this class.
 */
public class Closet {
    private Map<String, Map<String,String>> clothing;
    private Map<String, Integer> clothingStats;

    private ArrayList validClothesNames;
    private ArrayList validTopNames;
    private ArrayList validBottomNames;
    private ArrayList validShoeNames;
    private ArrayList validOuterNames;

    private int tops;
    private int bottoms;
    private int shoes;
    private int outer;

    public Closet(){
        this.clothing = new HashMap<>();
        this.clothingStats = new HashMap<>();

        this.tops = 0;
        this.bottoms = 0;
        this.shoes = 0;
        this.outer = 0;

        this.validClothesNames = new ArrayList<>(Arrays.asList("jeans", "pants", "sweats", "shorts", "skirt",
                "top", "hoodie", "shirt", "long-sleeve", "short-sleeve", "sweater", "tank",
                "jacket", "coat", "hoodie", "shoes", "sneakers", "boots"));

        this.validTopNames = new ArrayList<>(Arrays.asList("top", "tank", "hoodie", "shirt", "long-sleeve", "short-sleeve", "sweater"));
        this.validBottomNames = new ArrayList<>(Arrays.asList("jeans", "pants", "sweats", "shorts", "skirt"));
        this.validOuterNames = new ArrayList<>(Arrays.asList("jacket", "coat", "hoodie"));
        this.validShoeNames = new ArrayList<>(Arrays.asList("shoes", "sneakers", "boots"));

        this.clothingStats.put("Tops", this.tops);
        this.clothingStats.put("Bottoms", this.bottoms);
        this.clothingStats.put("Shoes", this.shoes);
        this.clothingStats.put("Outer", this.outer);
    }

    /**
     * Method adds clothing into the closet. Checks which of the clothing categories it fits into
     * and adds it into the closet respectively
     * @param color
     * @param name
     */
    public void addClothing(String color, String name){

        if(checkValidity(this.validTopNames,name)){
            this.tops++;
        } else if (checkValidity(this.validBottomNames, name)){
            this.bottoms++;
        } else if (checkValidity(this.validShoeNames, name)){
            this.shoes++;
        } else if (checkValidity(this.validOuterNames, name)){
            this.outer++;
        }

        String clothingName = color+" "+name;
        try {
            Map<String, String> colorItemMap = new HashMap<>();
            colorItemMap.put("item",name);
            colorItemMap.put("color",color);
            this.clothing.put(clothingName, colorItemMap);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
  }

      /**
       * Method removes clothes from the closet
       *
       * @param color
       * @param name
       */
      public void removeClothing(String color, String name) {

          if(checkValidity(this.validTopNames,name)){
              this.tops--;
          } else if (checkValidity(this.validBottomNames, name)){
              this.bottoms--;
          } else if (checkValidity(this.validShoeNames, name)){
              this.shoes--;
          } else if (checkValidity(this.validOuterNames, name)){
              this.outer--;
          }

          String clothingName = color + " " + name;
          for (String clothes : this.clothing.keySet()) {
              if (clothingName.equals(clothes)) {
                  this.clothing.remove(clothingName);
                  break;
              }
          }
      }

    /**
     * Method used to validate whether the item of clothing the user typed is a valid clothing item to
     * add into the closet
     *
     * @param clothes valid names for clothing items
     * @param name name the user types
     * @return
     */
    private boolean checkValidity(List clothes, String name) {
        if (clothes.contains(name)) {
            return true;
        }
        return false;
    }

    public Map<String, Integer> getStats(){
        this.clothingStats.put("Tops", this.tops);
        this.clothingStats.put("Bottoms", this.bottoms);
        this.clothingStats.put("Shoes", this.shoes);
        this.clothingStats.put("Outer", this.outer);
        return this.clothingStats;
    }

      /**
       * @return List of dictionaries of clothing data
       */
    public List<Map<String, String>> getClothesData() {
        List<Map<String, String>> clothesList = new ArrayList<>();
        clothesList.addAll(this.clothing.values());
        return clothesList;
    }

}
