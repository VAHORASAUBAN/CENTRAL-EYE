package com.example.integration.activities.user;

import android.Manifest;
import android.content.IntentSender;
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
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class User_Add_Product_Scanner extends Fragment {
    private static final int REQUEST_CHECK_SETTINGS = 1002;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;
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

        // Check camera and location permissions
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            checkLocationSettings(); // Check location settings
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
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

    private void checkLocationSettings() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(5000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true);

        SettingsClient settingsClient = LocationServices.getSettingsClient(requireContext());
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    task.getResult(ApiException.class);
                    Toast.makeText(requireContext(), "Location is enabled", Toast.LENGTH_SHORT).show();
                } catch (ResolvableApiException e) {
                    try {
                        e.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        Toast.makeText(requireContext(), "Failed to prompt location settings.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Error checking location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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
