package edu.brown.cs.student.outfit;

public class ClothingItem {
    private final String name;
    private final ItemKind itemKind;

    public ClothingItem(String name, ItemKind itemKind) {
        this.name = name;
        this.itemKind = itemKind;
    }

    public String getName(){
        return this.name;
    }

    public ItemKind getItemKind(){
        return this.itemKind;
    }

}
