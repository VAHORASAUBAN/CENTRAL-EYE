package com.example.integration.api;

public class AssignProduct {

    private String barcode;
    private String return_date;
    private String username;     // New field for username

    // Constructor (updated to use only three parameters)
    public AssignProduct(String barcode, String returnDate, String username) {
        this.barcode = barcode;
        this.return_date = returnDate;
        this.username = username;
    }

    // Getters and Setters for all fields
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getReturn_date() {
        return return_date;
    }

    public void setReturn_date(String return_date) {
        this.return_date = return_date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
