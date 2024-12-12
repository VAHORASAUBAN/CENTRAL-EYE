package com.example.integration.activities.admin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.integration.R;
import com.example.integration.activities.MainActivity;
import com.example.integration.activities.ProductListFragment;
import com.example.integration.activities.SearchScanner;
import com.example.integration.activities.model.Product;
import com.example.integration.activities.user.User_Profile_fragment;
import com.example.integration.api.ApiService;
import com.example.integration.network.RetrofitClient;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Remaining_barcodescanner extends Fragment {

    private static final String ARG_PRODUCT = "product";
    private Product product;
    private DecoratedBarcodeView barcodeScannerView;
    private TextView scannedValueTv;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;
    private ImageButton flashlightButton;
    private boolean isFlashlightOn = false;

    private boolean isProcessingBarcode = false;

    public Remaining_barcodescanner() {
        // Required empty public constructor
    }

    public static Remaining_barcodescanner newInstance(Product product) {
        Remaining_barcodescanner fragment = new Remaining_barcodescanner();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PRODUCT, product); // Pass Product object as Parcelable
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = getArguments().getParcelable(ARG_PRODUCT); // Retrieve Product object as Parcelable
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_remaining_barcodescanner, container, false);
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

        if (barcodeScannerView == null) {
            throw new IllegalStateException("DecoratedBarcodeView (barcodeScannerView) is null. Check the ID in the layout.");
        }

        // Check camera permissions
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startScanning(); // Start scanning if permission is granted
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }


        flashlightButton.setOnClickListener(v -> toggleFlashlight());


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
        if (isProcessingBarcode) {
            return; // Ignore if already processing
        }

        isProcessingBarcode = true; // Set the flag to true

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Create a map for the request body
        Map<String, String> barcodeData = new HashMap<>();
        barcodeData.put("barcode", scannedValue); // Key must match the serializer's field

        Call<Void> call = apiService.updateProductBarcode(product.getAsset_id(), barcodeData);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                isProcessingBarcode = false; // Reset the flag
                if (response.isSuccessful()) {
                    // Update success
                    Toast.makeText(getContext(), "Barcode added successfully!", Toast.LENGTH_LONG).show();
                    redirectToProductListFragment(); // Redirect after success
                } else {
                    // Update failed
                    Toast.makeText(getContext(), "Failed to update barcode. Please try again.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                isProcessingBarcode = false; // Reset the flag
                // Handle API failure
                Toast.makeText(getContext(), "An error occurred while updating the barcode.", Toast.LENGTH_LONG).show();
            }
        });
    }





    private void redirectToProductListFragment() {
        // Replace the current fragment with ProductListFragment
        ProductListFragment productListFragment = new ProductListFragment();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, productListFragment)
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
