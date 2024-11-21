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

public class Add_Product_Scanner extends Fragment {

    private DecoratedBarcodeView barcodeScannerView;
    private TextView scannedValueTv;

    public Add_Product_Scanner() {
        // Required empty public constructor
    }

    public static Add_Product_Scanner newInstance(String param1, String param2) {
        Add_Product_Scanner fragment = new Add_Product_Scanner();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        barcodeScannerView = view.findViewById(R.id.barcode_scanner);
        scannedValueTv = view.findViewById(R.id.scannedValueTv);

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
        // Start the scanner
        barcodeScannerView.resume();
        Toast.makeText(getContext(), "Scanner is ready.", Toast.LENGTH_SHORT).show();
    }

    private void handleScannedValue(String scannedValue) {
        // Navigate to the form fragment with the scanned value
        Scanner_Form_DetailsFragment formDetailsFragment = Scanner_Form_DetailsFragment.newInstance(scannedValue);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, formDetailsFragment) // Replace with your container ID
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        barcodeScannerView.resume(); // Resume scanning when fragment is visible
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeScannerView.pause(); // Pause scanning when fragment is not visible
    }
}
