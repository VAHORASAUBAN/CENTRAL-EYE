package com.example.integration.api;

public class LoginResponse {
    private String message;
    private String role;
    private String username;
    private String error;  // Optional, depends on the API response structure

    public String getMessage() {
        return message;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getError() {
        return error;
    }
}
