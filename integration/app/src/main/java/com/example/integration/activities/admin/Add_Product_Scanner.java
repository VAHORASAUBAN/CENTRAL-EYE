package com.example.integration.activities.admin;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.integration.R;
import com.example.integration.activities.MainActivity;
import com.example.integration.activities.SearchScanner;
import com.example.integration.activities.user.User_Profile_fragment;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class Add_Product_Scanner extends Fragment {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;
    private static final int REQUEST_CHECK_SETTINGS = 1002;

    private DecoratedBarcodeView barcodeScannerView;
    private TextView scannedValueTv;
    private ImageButton flashlightButton;
    private boolean isFlashlightOn = false;


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
        ImageView profileImageButton = view.findViewById(R.id.profile_image);
        ImageButton scanner_icon = view.findViewById(R.id.scanner_icon);
        barcodeScannerView = view.findViewById(R.id.barcode_scanner);
        flashlightButton = view.findViewById(R.id.flashlight_button);

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

        scanner_icon.setOnClickListener(v -> {
            // Navigate to ProductListAddFragment
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new SearchScanner())
                    .addToBackStack(null) // Optional, adds transaction to back stack
                    .commit();
        });

        profileImageButton.setOnClickListener(v -> {
            // PopupMenu logic here...
            PopupMenu popupMenu = new PopupMenu(requireContext(), profileImageButton);
            popupMenu.getMenuInflater().inflate(R.menu.profile_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.menu_profile) {
                    openuserprofileFragment();
                    return true;
                } else if (id == R.id.menu_logout) {
                    performLogout();
                    return true;
                } else {
                    return false;
                }
            });

            popupMenu.show();
        });





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
        // Navigate to the form fragment with the scanned value
        Scanner_Form_DetailsFragment formDetailsFragment = Scanner_Form_DetailsFragment.newInstance(scannedValue);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, formDetailsFragment)
                .addToBackStack(null)
                .commit();
    }

    private void checkLocationSettings() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(5000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true);

        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(requireContext())
                .checkLocationSettings(builder.build());

        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    task.getResult(ApiException.class);
                    // Location settings are satisfied, start scanning
                    startScanning();
                    flashlightButton.setOnClickListener(v -> toggleFlashlight());

                } catch (ResolvableApiException e) {
                    // Location settings are not satisfied but can be resolved
                    try {
                        e.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        Toast.makeText(getContext(), "Failed to prompt location settings.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    showLocationRequiredDialog(); // Show dialog when user denies
                }
            }
        });
    }

    private void showLocationRequiredDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Location Required")
                .setMessage("This feature requires location services to be enabled. Please enable location to proceed.")
                .setCancelable(false)
                .setPositiveButton("Enable Location", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkLocationSettings(); // Retry location settings prompt
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Location is required. Exiting scanner.", Toast.LENGTH_LONG).show();
                        requireActivity().getSupportFragmentManager().popBackStack(); // Exit the scanner fragment
                    }
                })
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            barcodeScannerView.resume(); // Resume scanning when fragment is visible
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeScannerView.pause(); // Pause scanning when fragment is not visible
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, check location settings
                checkLocationSettings();
            } else {
                // Permission denied
                Toast.makeText(getContext(), "Camera permission is required to use the scanner.", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void performLogout() {
        SharedPreferences.Editor editor = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE).edit();
        editor.clear(); // Clear session
        editor.apply();

        Toast.makeText(requireContext(), "Logged Out Successfully", Toast.LENGTH_SHORT).show();

        // Navigate to login screen (optional)
        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void openuserprofileFragment() {
        User_Profile_fragment userprofileFragment = new User_Profile_fragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, userprofileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void toggleFlashlight() {
        if (barcodeScannerView != null) {
            if (isFlashlightOn) {
                barcodeScannerView.setTorchOff();
                isFlashlightOn = false;
                flashlightButton.setImageResource(R.drawable.baseline_flashlight_off_24); // Replace with appropriate icon
                Toast.makeText(requireContext(), "Flashlight turned off", Toast.LENGTH_SHORT).show();
            } else {
                barcodeScannerView.setTorchOn();
                isFlashlightOn = true;
                flashlightButton.setImageResource(R.drawable.baseline_flashlight_on_24); // Replace with appropriate icon
                Toast.makeText(requireContext(), "Flashlight turned on", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Scanner view is not initialized.", Toast.LENGTH_SHORT).show();
        }
    }




}
