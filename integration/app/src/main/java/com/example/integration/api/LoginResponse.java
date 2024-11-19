package com.example.integration.api;

public class LoginResponse {
    private String message;
    private String error;  // Optional, depends on the API response structure

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }
}