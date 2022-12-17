package edu.brown.cs.student.closet;

import java.util.*;

/**
 * Class represents the closet for the user. Methods to add and remove items for the closet are in
 * this class.
 */
public class Closet {
    private Map<String, Map<String,String>> clothing;
    private Map<String, Integer> clothingStats;

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

        this.validTopNames = new ArrayList<>(Arrays.asList("short-sleeve", "long-sleeve", "tank", "sweatshirt"));
        this.validBottomNames = new ArrayList<>(Arrays.asList("pants", "shorts", "skirt"));
        this.validOuterNames = new ArrayList<>(Arrays.asList("hoodie", "jacket", "coat"));
        this.validShoeNames = new ArrayList<>(Arrays.asList("sneakers", "boots"));

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

        String clothingName = color+" "+name;
        try {
            if(!(this.clothing.containsKey(clothingName))) {

                if(checkValidity(this.validTopNames,name)){
                    this.tops++;
                } else if (checkValidity(this.validBottomNames, name)){
                    this.bottoms++;
                } else if (checkValidity(this.validShoeNames, name)){
                    this.shoes++;
                } else if (checkValidity(this.validOuterNames, name)){
                    this.outer++;
                }

                Map<String, String> colorItemMap = new HashMap<>();
                colorItemMap.put("item", name);
                colorItemMap.put("color", color);
                this.clothing.put(clothingName, colorItemMap);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }


      /**
       * Method removes clothes from the closet
       *
       * @param color
       * @param name
       */
      public void removeClothing(String color, String name) {
          String clothingName = color + " " + name;

          for (String clothes : this.clothing.keySet()) {

              if (clothingName.equals(clothes)) {
                  this.clothing.remove(clothingName);

                  if(checkValidity(this.validTopNames,name)) {
                      this.tops--;
                  }
                  else if (checkValidity(this.validBottomNames, name)){
                      this.bottoms--;
                  } else if (checkValidity(this.validShoeNames, name)){
                      this.shoes--;
                  } else if (checkValidity(this.validOuterNames, name)){
                      this.outer--;
                  }
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

    private void setZero(){
        if(this.tops<0){
            this.tops = 0;

        }if(this.bottoms<0){
            this.bottoms = 0;

        }if(this.outer<0){
            this.outer = 0;

        }if(this.shoes<0){
            this.shoes = 0;
        }
    }

    public Map<String, Integer> getStats(){
        this.setZero();
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
