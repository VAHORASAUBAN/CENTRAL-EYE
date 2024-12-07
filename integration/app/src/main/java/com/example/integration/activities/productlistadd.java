package com.example.integration.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.integration.R;
import com.example.integration.api.ApiService;
import com.example.integration.network.RetrofitClient;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class productlistadd extends Fragment {

    public productlistadd() {
        // Required empty public constructor
    }

    public static productlistadd newInstance(String param1, String param2) {
        productlistadd fragment = new productlistadd();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productlistadd, container, false);

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.productRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Retrofit and API service
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Fetch products from the backend
        apiService.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> productList = response.body();

                    // Set adapter
                    ProductAdapter adapter = new ProductAdapter(getContext(), productList, product -> {
                        // Handle item click
                        navigateToProductDescription(product);
                    });
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Failed to load products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    private void navigateToProductDescription(Product product) {
        ProductDescriptionFragment fragment = ProductDescriptionFragment.newInstance(
                product.getAsset_name(),
                product.getBarcode(),
                product.getPurchase_date(),
                product.getAsset_category(),
                product.getAsset_value(),
                product.getCondition(),
                product.getLocation()
        );

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
