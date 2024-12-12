package com.example.integration.activities.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private String asset_name;
    private String barcode;
    private String asset_category;
    private String asset_sub_category;
    private String purchase_date;
    private String asset_value;
    private String condition;
    private String location;
    private String assign_to;
    private String asset_id;

    private String username;
    // Constructor
    public Product(String asset_name, String barcode, String asset_category, String purchase_date, String asset_value, String condition, String location) {
        this.asset_name = asset_name;
        this.barcode = barcode;
        this.asset_category = asset_category;
        this.purchase_date = purchase_date;
        this.asset_value = asset_value;
        this.condition = condition;
        this.location = location;
    }

    // Getters and Setters
    public String getAsset_name() {
        return asset_name;
    }
    public String getAsset_id() {
        return asset_id;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getAsset_sub_category() {
        return asset_sub_category;
    }

    public String getAsset_category() {
        return asset_category;
    }

    public String getPurchase_date() {
        return purchase_date;
    }

    public String getAsset_value() {
        return asset_value;
    }

    public String getCondition() {
        return condition;
    }

    public String getLocation() {
        return location;
    }

    public String getAssign_to() {
        return assign_to;
    }

    public String getUsername() {
        return username;  // Getter for username
    }

    // Parcelable implementation
    protected Product(Parcel in) {
        asset_name = in.readString();
        barcode = in.readString();
        asset_category = in.readString();
        asset_sub_category = in.readString();
        purchase_date = in.readString();
        asset_category = in.readString();
        condition = in.readString();
        location = in.readString();
        assign_to = in.readString();
        username = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(asset_name);
        dest.writeString(barcode);
        dest.writeString(asset_category);
        dest.writeString(purchase_date);
        dest.writeString(asset_category);
        dest.writeString(asset_sub_category);
        dest.writeString(condition);
        dest.writeString(location);
        dest.writeString(assign_to);
        dest.writeString(username);
    }
}