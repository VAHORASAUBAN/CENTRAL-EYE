package com.example.integration.api;

public class LoginResponse {
    private String message;
    private String role;
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
    public String getError() {
        return error;
    }
}