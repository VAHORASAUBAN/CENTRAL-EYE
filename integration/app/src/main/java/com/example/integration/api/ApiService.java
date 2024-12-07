package com.example.integration.api;

import com.example.integration.activities.Product;
import com.example.integration.activities.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    // Define the login endpoint
    @POST("api/login/")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("api/totals/") // This should match your backend URL endpoint
    Call<TotalsResponse> getTotals();

    // Define the product save endpoint
    @POST("api/add_product/")
    Call<Void> saveProductDetails(@Body ProductDetails productDetails);

    @POST("api/assign_product/")
    Call<Void> saveAssignProduct(@Body AssignProduct assignProduct);

    @GET("api/asset/")  // Ensure the endpoint matches your Django URL
    Call<List<Product>> getProducts();

    // Fetch products with a specific filter (e.g., available)
    @GET("api/asset/")
    Call<List<Product>> getProductsWithFilter(@Query("filter") String filter);

    @GET("api/users/") // Replace with your endpoint
    Call<List<User>> getUsers();

}
