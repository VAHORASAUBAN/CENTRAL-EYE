package com.example.integration.api;

public class ProductDetails {
    private String barcode;
    private String asset_type;
    private String asset_name;
    private String purchase_date;
    private String asset_value;
    private String condition;

    // Constructor
    public ProductDetails(String barcode, String assetType, String assetName, String purchaseDate, String assetValue, String condition) {
        this.barcode = barcode;
        this.asset_type = assetType;
        this.asset_name = assetName;
        this.purchase_date = purchaseDate;
        this.asset_value = assetValue;
        this.condition = condition;
    }

    // Getters and Setters (if needed)
}
