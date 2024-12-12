package com.example.integration.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.integration.R;
import com.example.integration.activities.model.Product;
import com.example.integration.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        if (product == null) {
            Log.e("ProductDescriptionFragment", "Product is null, unable to display details.");
            return view; // Return early or show a default message
        }

        // Continue with binding the data to views only if product is not null
        Log.d("Productname", "Product Name: " + product.getAsset_name());
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


        // Display user details or appropriate message
        LinearLayout topLayout = view.findViewById(R.id.topLayout);
        TextView userDetailsSection = view.findViewById(R.id.userDetailsSection);
        LinearLayout userInfoCard = view.findViewById(R.id.userInfoCard);

        if (product.getAssign_to() == null || product.getAssign_to().isEmpty()) {
            userDetailsSection.setVisibility(View.VISIBLE);
            userDetailsSection.setText("This product is not assigned to any user.");
            userInfoCard.setVisibility(View.GONE); // Hide the user details card
        } else {
            userDetailsSection.setVisibility(View.GONE);
            userInfoCard.setVisibility(View.VISIBLE); // Show the user details card
            ((TextView) view.findViewById(R.id.usernameValue)).setText(product.getAssign_to());
            ((TextView) view.findViewById(R.id.locationValue)).setText(product.getLocation());
            ((TextView) view.findViewById(R.id.returnDateValue)).setText("Set return date logic here");
        }

        // Handle back button click
        ImageView backBtn = view.findViewById(R.id.backbtn);
        backBtn.setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ProductListFragment())
                    .addToBackStack(null) // Optional, adds transaction to back stack
                    .commit();
        });


        String condition = product.getCondition();

        int colorResId; // To store the resolved color resource
        switch (condition) {
            case "good":
                colorResId = R.drawable.top_background;
                break;
            case "average":
                colorResId = R.drawable.top_background_3;
                break;
            case "below-average":
                colorResId = R.drawable.top_background_2;
                break;
            default:
                colorResId =  R.drawable.rounded_border_rect4;
                break;
        }

        // Set the background color dynamically
        topLayout.setBackgroundResource(colorResId);


        return view;
    }
}