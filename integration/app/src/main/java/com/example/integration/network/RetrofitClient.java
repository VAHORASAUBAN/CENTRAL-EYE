package com.example.integration.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

<<<<<<< HEAD
    private static final String BASE_URL = "http://192.168.120.155:8000/"; // Replace with your backend URL
=======
    private static final String BASE_URL = "http://192.168.84.222:8000/"; //sauban Replace with your backend URL

//    private static final String BASE_URL = "http://192.168.116.22:8000/"; // ayan Replace with your backend URL
>>>>>>> 76cd60559e5013f6f9646983e06ee625b43f2259
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