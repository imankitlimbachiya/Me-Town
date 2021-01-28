package com.app.metown.Models;

public class CategoryModel {

    String CategoryID, CategoryName, CategoryTitle, CategoryImage, CategoryParentID, CategoryParentCategoryTitle,
            CategoryType, CategoryStatus, Images;

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryTitle() {
        return CategoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        CategoryTitle = categoryTitle;
    }

    public String getCategoryImage() {
        return CategoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        CategoryImage = categoryImage;
    }

    public String getCategoryParentID() {
        return CategoryParentID;
    }

    public void setCategoryParentID(String categoryParentID) {
        CategoryParentID = categoryParentID;
    }

    public String getCategoryParentCategoryTitle() {
        return CategoryParentCategoryTitle;
    }

    public void setCategoryParentCategoryTitle(String categoryParentCategoryTitle) {
        CategoryParentCategoryTitle = categoryParentCategoryTitle;
    }

    public String getCategoryType() {
        return CategoryType;
    }

    public void setCategoryType(String categoryType) {
        CategoryType = categoryType;
    }

    public String getCategoryStatus() {
        return CategoryStatus;
    }

    public void setCategoryStatus(String categoryStatus) {
        CategoryStatus = categoryStatus;
    }

    public String getImages() {
        return Images;
    }

    public void setImages(String images) {
        Images = images;
    }
}
