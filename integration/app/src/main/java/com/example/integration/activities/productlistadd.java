package com.example.integration.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.integration.R;


import java.util.ArrayList;
import java.util.List;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_productlistadd, container, false);

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.productRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create sample product data
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Product 1", "Barcode1", "001"));
        productList.add(new Product("Product 2", "Barcode2", "002"));
        productList.add(new Product("Product 3", "Barcode3", "003"));

        // Set adapter
        ProductAdapter adapter = new ProductAdapter(getContext(), productList, product -> {
            // Handle item click
            navigateToProductDescription(product);
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void navigateToProductDescription(Product product) {
        // Create a new instance of ProductDescriptionFragment with arguments
        ProductDescriptionFragment fragment = ProductDescriptionFragment.newInstance(
                product.getName(), product.getBarcode()
        );

        // Start a transaction to replace the fragment
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment) // Ensure this ID matches your container
                .addToBackStack(null)
                .commit();
    }
}
