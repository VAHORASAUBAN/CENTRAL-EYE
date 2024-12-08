package com.example.integration.activities;

public class RequestModel {
    private String requestId;
    private String userName;


    public RequestModel(String requestId, String userName) {
        this.requestId = requestId;
        this.userName = userName;

    }

    public String getRequestId() { return requestId; }
    public String getUserName() { return userName; }
}

