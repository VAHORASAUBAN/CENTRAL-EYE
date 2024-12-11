package com.example.integration.activities.model;

public class RequestItemModel {
    private String assetName;
    private int totalAssets;
    private int availableAssets;
    private String returnDate;

    public RequestItemModel(String assetName, int totalAssets, int availableAssets, String returnDate) {
        this.assetName = assetName;
        this.totalAssets = totalAssets;
        this.availableAssets = availableAssets;
        this.returnDate = returnDate;
    }

    public String getAssetName() { return assetName; }
    public int getTotalAssets() { return totalAssets; }
    public int getAvailableAssets() { return availableAssets; }
    public String getReturnDate() { return returnDate; }
}

