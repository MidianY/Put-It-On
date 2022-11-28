package edu.brown.cs.student;

import edu.brown.cs.student.outfit.Closet;
import edu.brown.cs.student.outfit.ClothingItem;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;

public class ClosetTest {

    public ClosetTest(){
    }

    @Test
    public void createCloset(){
        Closet closet = new Closet();
        closet.addBottom("blue jeans");
        System.out.println(closet.getClothes());

    }


}
