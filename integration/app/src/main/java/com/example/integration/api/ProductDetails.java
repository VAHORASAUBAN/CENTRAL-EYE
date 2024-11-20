package com.example.integration.api;

public class ProductDetails {
    private String barcode;
    private String product_name;
    private String product_price;

    // Constructor
    public ProductDetails(String barcode, String productName, String productPrice) {
        this.barcode = barcode;
        this.product_name = productName;
        this.product_price = productPrice;
    }

    // Getters and Setters (if needed)
}
