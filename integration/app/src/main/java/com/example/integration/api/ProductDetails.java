package com.example.integration.api;

public class ProductDetails {
    private String barcode;
    private String asset_name;
    private String purchase_date;
    private String asset_value;
    private String condition;
    private String location;
    private String category;
    private String subcategory;

    // Constructor (updated to include location and other fields)
    public ProductDetails(String barcode, String assetName, String purchaseDate,
                          String assetValue, String condition, String location, String category, String subcategory) {
        this.barcode = barcode;
        this.asset_name = assetName;
        this.purchase_date = purchaseDate;
        this.asset_value = assetValue;
        this.condition = condition;
        this.location = location;
        this.category = category;
        this.subcategory = subcategory;
    }

    // Getters and Setters for all fields

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getAsset_name() {
        return asset_name;
    }

    public void setAsset_name(String asset_name) {
        this.asset_name = asset_name;
    }

    public String getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(String purchase_date) {
        this.purchase_date = purchase_date;
    }

    public String getAsset_value() {
        return asset_value;
    }

    public void setAsset_value(String asset_value) {
        this.asset_value = asset_value;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }
}
