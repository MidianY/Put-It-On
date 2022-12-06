package edu.brown.cs.student;

import edu.brown.cs.student.outfit.Closet;
import edu.brown.cs.student.outfit.ItemKind;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClosetTest {

    public ClosetTest(){
    }

    /**
     * Test to ensure that the closet can properly add clothing items that are
     * properly named
     */
    @Test
    public void createSimpleCloset(){
        Closet closet = new Closet();

        String color = "blue";
        String color2 = "red";
        String color3 = "green";

        String pants = "jeans";
        String shirt = "shirt";
        String shoes = "shoes";
        String outer = "coat";

        closet.addClothing(color,pants);
        closet.addClothing(color2, shirt);
        closet.addClothing(color3, shoes);
        closet.addClothing(color, outer);

        Map<String, ItemKind> expectedMap = new HashMap<>();
        expectedMap.put(color+" "+pants, ItemKind.BOTTOM);
        expectedMap.put(color2+" "+shirt, ItemKind.TOP);
        expectedMap.put(color3+" "+shoes, ItemKind.SHOES);
        expectedMap.put(color+" "+outer, ItemKind.OUTER);

        assertEquals(closet.getClothing().size(), 4);
        assertEquals(closet.getClothesMap(), expectedMap);
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

        List expectedCloset = new ArrayList<>();
        expectedCloset.add(color+" "+outer);
        expectedCloset.add(color2+" "+shirt);

        assertEquals(closet.getClothing().size(), 2);
        assertEquals(closet.getClothing(), expectedCloset);
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

        assertEquals(closet.getClothing().size(), 0);
    }
}
