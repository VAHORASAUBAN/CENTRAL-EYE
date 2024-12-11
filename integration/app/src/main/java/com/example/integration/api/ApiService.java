package com.example.integration.api;

import com.example.integration.activities.model.Product;
import com.example.integration.activities.model.RequestModel;
import com.example.integration.activities.model.User;
import com.example.integration.activities.model.Category;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // Define the login endpoint
    @POST("api/login/")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("api/categories/")
    Call<List<Category>> getCategories();

    @GET("api/conditions/")
    Call<List<String>> getConditionChoices();

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

    @GET("api/products/{barcode}/")
    Call<Product> getProductByBarcode(@Path("barcode") String barcode);

    @PUT("api/update-barcode/{asset_id}/")
    Call<Void> updateProductBarcode(@Path("asset_id") String assetId, @Body Map<String, String> barcodeData);

    @GET("api/requests/")
    Call<List<RequestModel>> getRequests();

}