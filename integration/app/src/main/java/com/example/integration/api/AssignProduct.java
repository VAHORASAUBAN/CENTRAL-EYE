package com.example.integration.api;

public class AssignProduct {

    private String barcode;
    private String return_date;
    private String username;     // New field for username
    private String location;     // New field for location

    // Constructor (updated to use only three parameters)
    public AssignProduct(String barcode, String returnDateEditText, String username, String location) {
        this.barcode = barcode;
        this.return_date = returnDateEditText;
        this.username = username;
        this.location = location;
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

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

}
