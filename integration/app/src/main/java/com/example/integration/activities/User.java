package com.example.integration.activities;

public class User {
    private final String name;
    private final String title;
    private final String location;
    private final int imageResId;

    public User(String name, String title, String location, int imageResId) {
        this.name = name;
        this.title = title;
        this.location = location;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public int getImageResId() {
        return imageResId;
    }
}
