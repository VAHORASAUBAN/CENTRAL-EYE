package com.example.integration.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.integration.R;
import com.example.integration.activities.model.Product;
import com.example.integration.api.ApiService;
import com.example.integration.network.RetrofitClient;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchScanner extends Fragment {
    private DecoratedBarcodeView barcodeScannerView;
    private TextView scannedValueTv;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;

    private boolean isProcessingBarcode = false;

    public SearchScanner() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_scanner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        barcodeScannerView = view.findViewById(R.id.search_scanner);
        scannedValueTv = view.findViewById(R.id.scannedValueTv);

        if (barcodeScannerView == null) {
            throw new IllegalStateException("DecoratedBarcodeView (barcodeScannerView) is null. Check the ID in the layout.");
        }

        // Check camera permissions
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startScanning(); // Start scanning if permission is granted
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    private void startScanning() {
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

        // Start the scanner
        barcodeScannerView.resume();
        Toast.makeText(getContext(), "Scanner is ready.", Toast.LENGTH_SHORT).show();
    }

    private void handleScannedValue(String scannedValue) {
        if (isProcessingBarcode) {
            return; // Ignore if already processing
        }

        isProcessingBarcode = true; // Set the flag to true

        // Use Retrofit to fetch product details by barcode
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Product> call = apiService.getProductByBarcode(scannedValue);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                isProcessingBarcode = false; // Reset the flag
                if (response.isSuccessful() && response.body() != null) {
                    Product product = response.body();
                    openProductDescriptionFragment(product);
                } else {
                    Toast.makeText(getContext(), "Product not found with this barcode.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                isProcessingBarcode = false; // Reset the flag
                Toast.makeText(getContext(), "An error occurred while fetching the product.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openProductDescriptionFragment(Product product) {
        // Pass the product details to the ProductDescriptionFragment
        ProductDescriptionFragment productDescriptionFragment = ProductDescriptionFragment.newInstance(product);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, productDescriptionFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (barcodeScannerView != null && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            barcodeScannerView.resume(); // Resume scanning
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (barcodeScannerView != null) {
            barcodeScannerView.pause(); // Pause scanning
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (barcodeScannerView != null) {
                    barcodeScannerView.resume();
                    startScanning();
                }
            } else {
                Toast.makeText(getContext(), "Camera permission is required to use the scanner.", Toast.LENGTH_LONG).show();
            }
        }
    }
}