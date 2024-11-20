package com.example.integration.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.integration.R;
import com.example.integration.api.ApiService;
import com.example.integration.network.RetrofitClient;
import com.example.integration.api.ProductDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Scanner_Form_Details extends Fragment {

    private static final String ARG_SCANNED_BARCODE = "scannedBarcode";

    private String scannedBarcode;
    private TextView barcodeTextView;
    private EditText productNameEditText;
    private EditText productPriceEditText;

    public Scanner_Form_Details() {
        // Required empty public constructor
    }

    public static Scanner_Form_Details newInstance(String scannedBarcode) {
        Scanner_Form_Details fragment = new Scanner_Form_Details();
        Bundle args = new Bundle();
        args.putString(ARG_SCANNED_BARCODE, scannedBarcode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            scannedBarcode = getArguments().getString(ARG_SCANNED_BARCODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scanner__form__details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        barcodeTextView = view.findViewById(R.id.barcodeTextView);
        productNameEditText = view.findViewById(R.id.productNameEditText);
        productPriceEditText = view.findViewById(R.id.productPriceEditText);

        // Set the scanned barcode
        if (scannedBarcode != null) {
            barcodeTextView.setText("Barcode: " + scannedBarcode);
        }

        // Handle save button click
        view.findViewById(R.id.saveButton).setOnClickListener(v -> saveProductDetails());
    }

    private void saveProductDetails() {
        String productName = productNameEditText.getText().toString().trim();
        String productPrice = productPriceEditText.getText().toString().trim();

        if (productName.isEmpty() || productPrice.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create ProductDetails object
        ProductDetails productDetails = new ProductDetails(scannedBarcode, productName, productPrice);

        // Initialize Retrofit and make the call
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        apiService.saveProductDetails(productDetails).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Product saved successfully!", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Failed to save product.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
