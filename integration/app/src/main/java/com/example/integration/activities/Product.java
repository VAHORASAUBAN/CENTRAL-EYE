package com.example.integration.activities;

public class Product {
    private String name;
    private String barcode;
    private String id;

    public Product(String name, String barcode, String id) {
        this.name = name;
        this.barcode = barcode;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getId() {
        return id;
    }
}

