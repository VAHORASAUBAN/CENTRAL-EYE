package com.example.integration.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    // Define the login endpoint
    @POST("api/token/")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
