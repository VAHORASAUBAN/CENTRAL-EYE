package com.example.integration.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.integration.R;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class User_Add_Product_Scanner extends Fragment {

    private DecoratedBarcodeView barcodeScannerView;
    private TextView scannedValueTv;

    public User_Add_Product_Scanner() {
        // Required empty public constructor
    }

    public static User_Add_Product_Scanner newInstance() {
        return new User_Add_Product_Scanner();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user__add__product__scanner, container, false); // Fixed the layout name to match a valid file
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        barcodeScannerView = view.findViewById(R.id.barcode_scanner);
        scannedValueTv = view.findViewById(R.id.scannedValueTv);

        // Ensure the UI components are correctly bound
        if (barcodeScannerView == null || scannedValueTv == null) {
            throw new IllegalStateException("Missing required views in layout: barcodeScannerView or scannedValueTv");
        }

        // Set up the barcode scanner callback
        barcodeScannerView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result != null && result.getText() != null) {
                    handleScannedValue(result.getText());
                }
            }

            @Override
            public void possibleResultPoints(java.util.List<com.google.zxing.ResultPoint> resultPoints) {
                // Optional: Handle possible result points (e.g., for UI feedback)
            }
        });

        // Start scanning automatically
        startScanning();
    }

    private void startScanning() {
        if (barcodeScannerView != null) {
            barcodeScannerView.resume(); // Start the scanner
            Toast.makeText(getContext(), "Scanner is ready.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Scanner view is not initialized.", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleScannedValue(String scannedValue) {
        if (scannedValue != null && !scannedValue.isEmpty()) {
            // Navigate to the form fragment with the scanned value
            User_Scanner_Form_DetailsFragment formDetailsFragment = User_Scanner_Form_DetailsFragment.newInstance(scannedValue);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, formDetailsFragment) // Replace with your container ID
                    .addToBackStack(null)
                    .commit();
        } else {
            Toast.makeText(getContext(), "Scanned value is empty.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (barcodeScannerView != null) {
            barcodeScannerView.resume(); // Resume scanning when fragment is visible
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (barcodeScannerView != null) {
            barcodeScannerView.pause(); // Pause scanning when fragment is not visible
        }
    }
}
