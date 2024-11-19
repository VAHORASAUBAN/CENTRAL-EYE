package com.example.integration.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.integration.R;

public class Scanner_Form_Details extends Fragment {

    private static final String ARG_SCANNED_BARCODE = "scannedBarcode";

    private String scannedBarcode;
    private TextView barcodeTextView;
    private EditText productNameEditText;
    private EditText productPriceEditText;
    private Button saveButton;

    public Scanner_Form_Details() {
        // Required empty public constructor
    }

    public static Scanner_Form_Details newInstance(String scannedBarcode, String param2) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scanner__form__details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        barcodeTextView = view.findViewById(R.id.barcodeTextView);
        productNameEditText = view.findViewById(R.id.productNameEditText);
        productPriceEditText = view.findViewById(R.id.productPriceEditText);
        saveButton = view.findViewById(R.id.saveButton);

        // Set the scanned barcode
        if (scannedBarcode != null) {
            barcodeTextView.setText("Barcode: " + scannedBarcode);
        }

        // Handle save button click
        saveButton.setOnClickListener(v -> saveProductDetails());
    }

    private void saveProductDetails() {
        String productName = productNameEditText.getText().toString().trim();
        String productPrice = productPriceEditText.getText().toString().trim();

        if (productName.isEmpty() || productPrice.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save the product details (e.g., to a database or shared preferences)
        Toast.makeText(getContext(), "Product saved:\nName: " + productName + "\nPrice: " + productPrice, Toast.LENGTH_SHORT).show();

        // Navigate back or reset fields
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
