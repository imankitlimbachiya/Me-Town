package com.app.metown.Models;

public class ItemMainModel {

    String ItemID, ItemName;

    public ItemMainModel(String ItemID, String ItemName) {
        this.ItemID = ItemID;
        this.ItemName = ItemName;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }
}
