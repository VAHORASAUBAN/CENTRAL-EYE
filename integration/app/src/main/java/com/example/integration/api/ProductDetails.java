package com.example.integration.api;

public class ProductDetails {
    private String barcode;
    private String asset_type;
    private String asset_name;
    private String purchase_date;
    private String asset_value;
    private String condition;
//    private String location;

    // Constructor (updated to include location)
    public ProductDetails(String barcode, String assetType, String assetName, String purchaseDate,
                          String assetValue, String condition) {
        this.barcode = barcode;
        this.asset_type = assetType;
        this.asset_name = assetName;
        this.purchase_date = purchaseDate;
        this.asset_value = assetValue;
        this.condition = condition;
//        this.location = Location;
    }

    // Getters and Setters for all fields

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getAsset_type() {
        return asset_type;
    }

    public void setAsset_type(String asset_type) {
        this.asset_type = asset_type;
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

//    public String getLocation() {
//        return location;
//    }
//
//    public void setLocation(String condition) {
//        this.location = location;
//    }
}
