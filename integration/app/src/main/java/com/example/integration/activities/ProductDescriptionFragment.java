package com.example.integration.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.integration.R;

public class ProductDescriptionFragment extends Fragment {

    private static final String ARG_NAME = "name";
    private static final String ARG_BARCODE = "barcode";
    private static final String ARG_PURCHASE_DATE = "purchase_date";
    private static final String ARG_ASSET_TYPE = "asset_type";
    private static final String ARG_ASSET_VALUE = "asset_value";
    private static final String ARG_CONDITION = "condition";
    private static final String ARG_LOCATION = "location";

    private String assetName;
    private String barcode;
    private String purchaseDate;
    private String assetType;
    private String assetValue;
    private String condition;
    private String location;

    public ProductDescriptionFragment() {
        // Required empty public constructor
    }

    public static ProductDescriptionFragment newInstance(String name, String barcode, String purchaseDate, String assetType, String assetValue, String condition, String location) {
        ProductDescriptionFragment fragment = new ProductDescriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_BARCODE, barcode);
        args.putString(ARG_PURCHASE_DATE, purchaseDate);
        args.putString(ARG_ASSET_TYPE, assetType);
        args.putString(ARG_ASSET_VALUE, assetValue);
        args.putString(ARG_CONDITION, condition);
        args.putString(ARG_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            assetName = getArguments().getString(ARG_NAME);
            barcode = getArguments().getString(ARG_BARCODE);
            purchaseDate = getArguments().getString(ARG_PURCHASE_DATE);
            assetType = getArguments().getString(ARG_ASSET_TYPE);
            assetValue = getArguments().getString(ARG_ASSET_VALUE);
            condition = getArguments().getString(ARG_CONDITION);
            location = getArguments().getString(ARG_LOCATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_description, container, false);

        // Bind data to TextViews
        ((TextView) view.findViewById(R.id.assetNameValue)).setText(assetName);
        ((TextView) view.findViewById(R.id.barcodeValue)).setText(barcode);
        ((TextView) view.findViewById(R.id.purchaseDateValue)).setText(purchaseDate);
        ((TextView) view.findViewById(R.id.assetTypeValue)).setText(assetType);
        ((TextView) view.findViewById(R.id.assetValue)).setText(assetValue);
        ((TextView) view.findViewById(R.id.conditionValue)).setText(condition);
//        ((TextView) view.findViewById(R.id.locationValue)).setText(location);

        ImageButton backBtn = view.findViewById(R.id.backbtn);
        backBtn.setOnClickListener(v -> {
            // Navigate to ProductListAddFragment
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ProductListFragment())
                    .addToBackStack(null) // Optional, adds transaction to back stack
                    .commit();
        });

        return view;
    }
}
