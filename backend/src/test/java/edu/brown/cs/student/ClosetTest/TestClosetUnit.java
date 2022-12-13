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

        assertEquals(closet.getClothesData().size(), 1);
        assertEquals(closet.getClothesData(), clothesList);
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


        assertEquals(closet.getClothesData().size(), 1);
        assertEquals(closet.getClothesData(), clothesList);
    }

    /**
     * Test to ensure that incorrectly named items won't be added
     * to the closet
     */
    @Test
    public void incorrectItems(){
        Closet closet = new Closet();

        String color1 = "ble";
        String color2 = "yelow";

        String shoes = "sneakes";
        String outer = "jacet";

        closet.addClothing(color1, shoes);
        closet.addClothing(color2, outer);

        assertEquals(closet.getClothesData().size(), 0);
    }
}
