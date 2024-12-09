package com.example.integration.activities;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.integration.R;
import com.example.integration.api.ApiService;
import com.example.integration.api.AssignProduct;
import com.example.integration.network.RetrofitClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_Scanner_Form_DetailsFragment extends Fragment {

    private FusedLocationProviderClient fusedLocationClient;

    private String mergedLocation;
    private static final String ARG_SCANNED_BARCODE = "scannedBarcode";

    private SharedPreferences sharedPreferences;
    private EditText returnDateEditText;
    private String scannedBarcode;
    private TextView barcodeTextView;

    public User_Scanner_Form_DetailsFragment() {
        // Required empty public constructor
    }

    public static User_Scanner_Form_DetailsFragment newInstance(String scannedBarcode) {
        User_Scanner_Form_DetailsFragment fragment = new User_Scanner_Form_DetailsFragment();
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
        sharedPreferences = requireContext().getSharedPreferences("Username", MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user__scanner__form__details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        getCurrentLocation();

        // Initialize views
        barcodeTextView = view.findViewById(R.id.barcodeTextView);
        returnDateEditText = view.findViewById(R.id.returnDateEditText);

        // Display scanned barcode
        if (scannedBarcode != null) {
            barcodeTextView.setText(scannedBarcode);
        }

        // Set up date picker for return date
        returnDateEditText.setOnClickListener(v -> showDatePickerDialog());

        // Set up save button click listener
        view.findViewById(R.id.saveButton).setOnClickListener(v -> saveAssignProduct());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    returnDateEditText.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveAssignProduct() {
        String returnDate = returnDateEditText.getText().toString().trim();

        // Use requireContext() or requireActivity() to get SharedPreferences in a Fragment
        sharedPreferences = requireContext().getSharedPreferences("UserSession", MODE_PRIVATE);

        // Fetch the username from SharedPreferences
        String username = sharedPreferences.getString("username", "User");

        // Validate inputs
        if (returnDate.isEmpty()) {
            Toast.makeText(getContext(), "Please select a return date.", Toast.LENGTH_SHORT).show();
            return;
        }

        AssignProduct assignProduct = new AssignProduct(scannedBarcode, returnDate, username, mergedLocation);
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Make API call
        apiService.saveAssignProduct(assignProduct).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Product assigned successfully!", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    try {
                        // Parse the error message from the response body
                        String errorMessage = response.errorBody() != null
                                ? new JSONObject(response.errorBody().string()).optString("message", "Something went wrong!")
                                : "Unknown error occurred!";

                        // Show the error message in a Toast
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Fallback error message if parsing fails
                        Toast.makeText(getContext(), "Failed to process error message!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API Failure", "Error: " + t.getMessage());
                Toast.makeText(getContext(), "Failed to assign product. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCurrentLocation() {
        // Check permissions
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        } else {
            // Permission granted, get location
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    // Combine latitude and longitude into a single variable
                    mergedLocation = latitude + "," + longitude;
                    Toast.makeText(getContext(), "Location captured: " + mergedLocation, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Unable to fetch location.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Error fetching location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}
