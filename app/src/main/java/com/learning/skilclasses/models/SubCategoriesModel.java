package com.learning.skilclasses.models;

public class SubCategoriesModel {
    private String subcategory;
    private String price;

    public SubCategoriesModel(String subcategory, String price) {
        this.subcategory = subcategory;
        this.price = price;
    }

    public SubCategoriesModel() {
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
