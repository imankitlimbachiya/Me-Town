package com.app.metown.Models;

public class ItemModel {

    String ItemID, ItemSellerID, ItemBuyerID, ItemCategoryID, ItemCategoryTitle, ItemName, ItemDescription, ItemStatus, ItemType, ItemPrice,
            ItemLatitude, ItemLongitude, ItemUpdatedAt, ItemIsNegotiable, ItemImages, ItemStatusTitle, ItemTypeTitle, ItemFavouriteCount,
            ItemCommentCount, Address, Distance, IsFavourite;

    public ItemModel() {

    }

    public ItemModel(String ItemID, String ItemName) {
        this.ItemID = ItemID;
        this.ItemName = ItemName;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public String getItemSellerID() {
        return ItemSellerID;
    }

    public void setItemSellerID(String itemSellerID) {
        ItemSellerID = itemSellerID;
    }

    public String getItemBuyerID() {
        return ItemBuyerID;
    }

    public void setItemBuyerID(String itemBuyerID) {
        ItemBuyerID = itemBuyerID;
    }

    public String getItemCategoryID() {
        return ItemCategoryID;
    }

    public void setItemCategoryID(String itemCategoryID) {
        ItemCategoryID = itemCategoryID;
    }

    public String getItemCategoryTitle() {
        return ItemCategoryTitle;
    }

    public void setItemCategoryTitle(String itemCategoryTitle) {
        ItemCategoryTitle = itemCategoryTitle;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public void setItemDescription(String itemDescription) {
        ItemDescription = itemDescription;
    }

    public String getItemStatus() {
        return ItemStatus;
    }

    public void setItemStatus(String itemStatus) {
        ItemStatus = itemStatus;
    }

    public String getItemType() {
        return ItemType;
    }

    public void setItemType(String itemType) {
        ItemType = itemType;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }

    public String getItemLatitude() {
        return ItemLatitude;
    }

    public void setItemLatitude(String itemLatitude) {
        ItemLatitude = itemLatitude;
    }

    public String getItemLongitude() {
        return ItemLongitude;
    }

    public void setItemLongitude(String itemLongitude) {
        ItemLongitude = itemLongitude;
    }

    public String getItemUpdatedAt() {
        return ItemUpdatedAt;
    }

    public void setItemUpdatedAt(String itemUpdatedAt) {
        ItemUpdatedAt = itemUpdatedAt;
    }

    public String getItemIsNegotiable() {
        return ItemIsNegotiable;
    }

    public void setItemIsNegotiable(String itemIsNegotiable) {
        ItemIsNegotiable = itemIsNegotiable;
    }

    public String getItemImages() {
        return ItemImages;
    }

    public void setItemImages(String itemImages) {
        ItemImages = itemImages;
    }

    public String getItemStatusTitle() {
        return ItemStatusTitle;
    }

    public void setItemStatusTitle(String itemStatusTitle) {
        ItemStatusTitle = itemStatusTitle;
    }

    public String getItemTypeTitle() {
        return ItemTypeTitle;
    }

    public void setItemTypeTitle(String itemTypeTitle) {
        ItemTypeTitle = itemTypeTitle;
    }

    public String getItemFavouriteCount() {
        return ItemFavouriteCount;
    }

    public void setItemFavouriteCount(String itemFavouriteCount) {
        ItemFavouriteCount = itemFavouriteCount;
    }

    public String getItemCommentCount() {
        return ItemCommentCount;
    }

    public void setItemCommentCount(String itemCommentCount) {
        ItemCommentCount = itemCommentCount;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getIsFavourite() {
        return IsFavourite;
    }

    public void setIsFavourite(String isFavourite) {
        IsFavourite = isFavourite;
    }
}