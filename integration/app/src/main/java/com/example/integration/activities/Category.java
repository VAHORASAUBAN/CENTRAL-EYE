package com.example.integration.activities;

import java.util.List;

public class Category {
    private int category_id;
    private String category_name;
    private List<Subcategory> subcategories;

    // Getter for category_id
    public int getCategoryId() {
        return category_id;
    }

    // Setter for category_id
    public void setCategoryId(int category_id) {
        this.category_id = category_id;
    }

    // Getter for category_name
    public String getCategoryName() {
        return category_name;
    }

    // Setter for category_name
    public void setCategoryName(String category_name) {
        this.category_name = category_name;
    }

    // Getter for subcategories
    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    // Setter for subcategories
    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }
}
