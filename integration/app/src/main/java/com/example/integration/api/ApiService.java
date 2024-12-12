package com.example.integration.api;

import com.example.integration.activities.model.Product;
import com.example.integration.activities.model.RequestModel;
import com.example.integration.activities.model.Subcategory;
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
    // Fetch subcategories for a specific category
    @GET("api/categories/{id}/")
    Call<List<Subcategory>> getSubcategories(@Path("id") int categoryId);
//    @GET("api/products/by-subcategory")
//    Call<List<Product>> getProductsBySubcategory(@Query("subcategoryId") int subcategoryId);
//    @GET("products/subcategory/{id}")
//    Call<List<Product>> productsBySubcategory(@Path("id") int subcategoryId);
    @GET("api/conditions/")
    Call<List<String>> getConditionChoices();

    @GET("api/totals/") // This should match your backend URL endpoint
    Call<TotalsResponse> getTotals();

    @GET("api/user/totals/")
    Call<TotalsResponse> getUserTotals(@Query("username") String username);

    // Define the product save endpoint
    @POST("api/add_product/")
    Call<Void> saveProductDetails(@Body ProductDetails productDetails);

    @POST("api/assign_product/")
    Call<Void> saveAssignProduct(@Body AssignProduct assignProduct);

    @GET("api/asset/")  // Ensure the endpoint matches your Django URL
    Call<List<Product>> getProducts();

    @GET("products/filter")
    Call<List<Product>> getProductsByCategoryAndSubcategory(
            @Query("category") String category,
            @Query("subcategory") String subcategory
    );

    // Fetch products with a specific filter (e.g., available)
    @GET("api/asset/")
    Call<List<Product>> getProductsWithFilter(@Query("filter") String filter);

    @GET("api/user/asset/")  // Ensure the endpoint matches your Django URL
    Call<List<Product>> getUserProducts(@Query("username") String username);
    @GET("api/user/asset/")
    Call<List<Product>> getUserProductsWithFilter(@Query("username") String username, @Query("filter") String filter);

    @GET("api/users/") // Replace with your endpoint
    Call<List<User>> getUsers();

    @GET("api/products/{barcode}/")
    Call<Product> getProductByBarcode(@Path("barcode") String barcode);

    @PUT("api/update-barcode/{asset_id}/")
    Call<Void> updateProductBarcode(@Path("asset_id") String assetId, @Body Map<String, String> barcodeData);

    @GET("api/requests/")
    Call<List<RequestModel>> getRequests();

}