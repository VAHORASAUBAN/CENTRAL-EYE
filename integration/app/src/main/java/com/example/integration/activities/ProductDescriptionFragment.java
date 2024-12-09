package com.example.integration.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.integration.R;
import com.example.integration.activities.model.Product;

public class ProductDescriptionFragment extends Fragment {

    private static final String ARG_PRODUCT = "product"; // Key for passing the Product object
    private Product product;

    public ProductDescriptionFragment() {
        // Required empty public constructor
    }

    // Updated newInstance method to accept a Product object
    public static ProductDescriptionFragment newInstance(Product product) {
        ProductDescriptionFragment fragment = new ProductDescriptionFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PRODUCT, product); // Put the Product object directly
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = getArguments().getParcelable(ARG_PRODUCT); // Retrieve the Product object
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_description, container, false);
        // Bind data to TextViews using the Product object
        Log.d("Productname", "Product Name"+product.getAsset_name());
        ((TextView) view.findViewById(R.id.assetNameValue)).setText(product.getAsset_name());
        ((TextView) view.findViewById(R.id.assetCategoryValue)).setText(product.getAsset_category());
        ((TextView) view.findViewById(R.id.assetSubCategoryValue)).setText(product.getAsset_sub_category());
        ((TextView) view.findViewById(R.id.assetNameValue)).setText(product.getAsset_name());
        ((TextView) view.findViewById(R.id.barcodeValue)).setText(product.getBarcode());
        ((TextView) view.findViewById(R.id.purchaseDateValue)).setText(product.getPurchase_date());
        ((TextView) view.findViewById(R.id.assetValue)).setText(product.getAsset_value());
        ((TextView) view.findViewById(R.id.conditionValue)).setText(product.getCondition());

        // Display the location if it's available
//        TextView locationTextView = view.findViewById(R.id.locationValue);
//        if (product.getLocation() != null && !product.getLocation().isEmpty()) {
//            locationTextView.setText(product.getLocation());
//        } else {
//            locationTextView.setText("Location not available");
//        }

        // Handle back button click
        ImageButton backBtn = view.findViewById(R.id.backbtn);
        backBtn.setOnClickListener(v -> {
            // Navigate to ProductListFragment
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ProductListFragment())
                    .addToBackStack(null) // Optional, adds transaction to back stack
                    .commit();
        });

        return view;
    }
}