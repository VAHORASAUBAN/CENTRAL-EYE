package com.example.integration.api;

import com.example.integration.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    // Define the login endpoint
    @POST("api/login/")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    // Define the product save endpoint
    @POST("api/add_product/")
    Call<Void> saveProductDetails(@Body ProductDetails productDetails);

    @POST("api/assign_product/")
    Call<Void> saveAssignProduct(@Body AssignProduct assignProduct);

    @GET("api/assets/")  // Endpoint to get list of products
    Call<List<Product>> getProductList();
}
