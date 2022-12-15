package edu.brown.cs.student.ClosetTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.brown.cs.student.closet.Closet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class TestClosetUnit {

    public TestClosetUnit(){
    }

    /**
     * Test to ensure that the closet can properly add clothing items that are
     * properly named
     */
    @Test
    public void createSimpleCloset(){
        Closet closet = new Closet();

        String color = "blue";
        String pants = "jeans";

        closet.addClothing(color,pants);

        Map<String, Map<String,String>> expectedResponse = new HashMap<>();

        Map<String,String> colorItem = new HashMap<>();
        colorItem.put("color", color);
        colorItem.put("item", pants);
        expectedResponse.put("clothing", colorItem);
        List<Map<String, String>> clothesList = new ArrayList<>();
        clothesList.addAll(expectedResponse.values());

        Map<String,Integer> clothesStats = new HashMap<>();
        clothesStats.put("Bottoms", 1);
        clothesStats.put("Tops", 0);
        clothesStats.put("Shoes", 0);
        clothesStats.put("Outer", 0);

        assertEquals(closet.getClothesData().size(), 1);
        assertEquals(closet.getClothesData(), clothesList);
        assertEquals(closet.getStats(), clothesStats);
    }

    /**
     * Test to ensure that you can properly remove items from the closet
     */
    @Test
    public void removeCloset() {
        Closet closet = new Closet();

        String color = "blue";
        String color2 = "yellow";
        String color3 = "orange";

        String pants = "jeans";
        String shirt = "tank";
        String shoes = "sneakers";
        String outer = "jacket";

        closet.addClothing(color,pants);
        closet.addClothing(color2, shirt);
        closet.addClothing(color3, shoes);
        closet.addClothing(color, outer);

        closet.removeClothing(color, pants);
        closet.removeClothing(color3, shoes);
        closet.removeClothing(color, outer);

        Map<String, Map<String,String>> expectedResponse = new HashMap<>();

        Map<String,String> colorItem = new HashMap<>();
        colorItem.put("color", color2);
        colorItem.put("item", shirt);

        expectedResponse.put("clothing", colorItem);

        List<Map<String, String>> clothesList = new ArrayList<>();
        clothesList.addAll(expectedResponse.values());

        Map<String,Integer> clothesStats = new HashMap<>();
        clothesStats.put("Bottoms", 0);
        clothesStats.put("Shoes", 0);
        clothesStats.put("Outer", 0);
        clothesStats.put("Tops", 1);

        assertEquals(closet.getClothesData().size(), 1);
        assertEquals(closet.getClothesData(), clothesList);
        assertEquals(closet.getStats(), clothesStats);
    }

    /**
     * Test to ensure that the closet is empty after adding items and removing those items from the closet
     */
    @Test
    public void emptyCloset(){
        Closet closet = new Closet();

        String color = "blue";
        String color2 = "yellow";
        String color3 = "orange";
        String color4 = "red";

        String pants = "jeans";
        String shirt = "tank";
        String shoes = "sneakers";
        String outer = "jacket";

        closet.addClothing(color,pants);
        closet.addClothing(color2, shirt);
        closet.addClothing(color3, shoes);
        closet.addClothing(color4, outer);

        closet.removeClothing(color, pants);
        closet.removeClothing(color2, shirt);
        closet.removeClothing(color3, shoes);
        closet.removeClothing(color4, outer);

        Map<String, Map<String,String>> expectedResponse = new HashMap<>();

        Map<String,String> colorItem = new HashMap<>();
        expectedResponse.put("clothing", colorItem);

        List<Map<String, String>> clothesList = new ArrayList<>();
        clothesList.addAll(expectedResponse.values());

        Map<String,Integer> clothesStats = new HashMap<>();
        clothesStats.put("Bottoms", 0);
        clothesStats.put("Shoes", 0);
        clothesStats.put("Outer", 0);
        clothesStats.put("Tops", 0);

        assertEquals(closet.getClothesData().size(), 0);
        assertEquals(closet.getStats(), clothesStats);
    }

    /**
     * Building a complex closet and ensuring adding and removing items will not mess
     * up thes structure of the closet
     */
    @Test
    public void complexCloset(){
        Closet closet = new Closet();

        String color = "blue";
        String color2 = "red";
        String color3 = "green";
        String color4 = "green";
        String color5 = "orange";
        String color6 = "beige";

        String pants = "jeans";
        String shirt = "sweater";
        String shirt2 = "tank";
        String shoes = "sneakers";
        String outer = "jacket";
        String outer2 = "coat";

        closet.addClothing(color,pants);
        closet.addClothing(color3, shoes);
        closet.addClothing(color4, outer);
        closet.addClothing(color5, shirt2);
        closet.addClothing(color6, outer2);
        closet.addClothing(color4, pants);
        closet.addClothing(color2, shirt);
        closet.addClothing(color, shoes);

        closet.removeClothing(color, pants);
        closet.removeClothing(color4, outer);
        closet.removeClothing(color3, shoes);
        closet.removeClothing(color4, pants);
        closet.removeClothing(color2, shirt);

        Map<String, Map<String,String>> expectedResponse = new HashMap<>();

        Map<String,String> colorItem = new HashMap<>();
        colorItem.put("color", color5);
        colorItem.put("item", shirt2);

        Map<String,String> colorItem2 = new HashMap<>();
        colorItem2.put("color", color6);
        colorItem2.put("item", outer2);

        Map<String,String> colorItem3 = new HashMap<>();
        colorItem3.put("color", color);
        colorItem3.put("item", shoes);

        expectedResponse.put("clothing", colorItem3);
        expectedResponse.put("clothing2", colorItem);
        expectedResponse.put("clothing3", colorItem2);

        List<Map<String, String>> clothesList = new ArrayList<>();
        clothesList.addAll(expectedResponse.values());

        Map<String,Integer> clothesStats = new HashMap<>();
        clothesStats.put("Bottoms", 0);
        clothesStats.put("Shoes", 1);
        clothesStats.put("Outer", 1);
        clothesStats.put("Tops", 1);

        assertEquals(closet.getClothesData().size(), 3);
        assertEquals(closet.getClothesData(), clothesList);
        assertEquals(closet.getStats(), clothesStats);
    }
}
