package com.example.integration.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.integration.R;

public class Product_fragment extends Fragment {

    private Button scanQrBtn;
    private TextView scannedValueTv;

    public Product_fragment() {
        // Required empty public constructor
    }

    public static Product_fragment newInstance(String param1, String param2) {
        Product_fragment fragment = new Product_fragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_fragment, container, false);

//        scanQrBtn = view.findViewById(R.id.scanQrBtn);  // Ensure to use your actual button ID
//        scannedValueTv = view.findViewById(R.id.scannedValueTv);  // TextView for displaying scanned value
//
//        // Set the click listener for the scan QR button
//        scanQrBtn.setOnClickListener(v -> {
//            ScanOptions options = new ScanOptions();
//            options.setPrompt("Scan QR Code");
//            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
//            // Launch the scanner
//            scannerLauncher.launch(options);
//        });

        return view;
    }

//    // Define the scanner launcher using registerForActivityResult
//    private final ActivityResultLauncher<ScanOptions> scannerLauncher = registerForActivityResult(
//            new ScanContract(),
//            result -> {
//                if (result.getContents() == null) {
//                    Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
//                } else {
//                    scannedValueTv.setText("Scanned Value: " + result.getContents());
//                }
//            }
//    );
}
