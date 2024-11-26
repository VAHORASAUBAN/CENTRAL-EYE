package com.example.integration.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://192.168.192.22:8000/"; //sauban Replace with your backend URL

    private static Retrofit retrofit = null;

    // Singleton instance of Retrofit
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            synchronized (RetrofitClient.class) { // Thread-safe singleton
                if (retrofit == null) { // Double-checked locking
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }
}