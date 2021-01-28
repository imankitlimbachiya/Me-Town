package com.app.metown.Models;

public class BusinessItemPriceModel {

    String ID, BusinessID, ServiceItem, PriceKind, Price, AdditionalInfo, IsMainMenu, CreatedAt;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getBusinessID() {
        return BusinessID;
    }

    public void setBusinessID(String businessID) {
        BusinessID = businessID;
    }

    public String getServiceItem() {
        return ServiceItem;
    }

    public void setServiceItem(String serviceItem) {
        ServiceItem = serviceItem;
    }

    public String getPriceKind() {
        return PriceKind;
    }

    public void setPriceKind(String priceKind) {
        PriceKind = priceKind;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getAdditionalInfo() {
        return AdditionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        AdditionalInfo = additionalInfo;
    }

    public String getIsMainMenu() {
        return IsMainMenu;
    }

    public void setIsMainMenu(String isMainMenu) {
        IsMainMenu = isMainMenu;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }
}
