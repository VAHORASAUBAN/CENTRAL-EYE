package com.example.integration.activities.admin;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.integration.R;
import com.example.integration.activities.ProductListFragment;
import com.example.integration.activities.model.Subcategory;
import com.example.integration.activities.model.Category;
import com.example.integration.api.ApiService;
import com.example.integration.network.RetrofitClient;
import com.example.integration.api.ProductDetails;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Scanner_Form_DetailsFragment extends Fragment {

    private FusedLocationProviderClient fusedLocationClient;

    private static final String ARG_SCANNED_BARCODE = "scannedBarcode";

    private EditText purchaseDateEditText;
    private String scannedBarcode;
    private TextView barcodeTextView;
    private EditText assetNameEditText;
    private EditText assetValueEditText;
    private AutoCompleteTextView conditionDropdown;
    private String mergedLocation;
    private AutoCompleteTextView categoryDropdown;
    private  AutoCompleteTextView subcategoryDropdown;

    public Scanner_Form_DetailsFragment() {
        // Required empty public constructor
    }

    public static Scanner_Form_DetailsFragment newInstance(String scannedBarcode) {
        Scanner_Form_DetailsFragment fragment = new Scanner_Form_DetailsFragment();
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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        getCurrentLocation();

        assetNameEditText = view.findViewById(R.id.model_name_input);
        barcodeTextView = view.findViewById(R.id.barcodeTextView);
        purchaseDateEditText = view.findViewById(R.id.purchaseDateEditText);
        assetValueEditText = view.findViewById(R.id.asset_value_input);
        conditionDropdown = view.findViewById(R.id.conditionValue);
        categoryDropdown = view.findViewById(R.id.category);
        subcategoryDropdown= view.findViewById(R.id.subcategory);

        // Set the scanned barcode
        if (scannedBarcode != null) {
            barcodeTextView.setText( scannedBarcode);
        }

        // Fetch and set up categories and subcategories
        fetchCategories(categoryDropdown, subcategoryDropdown);

        // Fetch conditions
        fetchConditions(conditionDropdown);

        // Handle date picker for purchaseDateEditText
        purchaseDateEditText.setOnClickListener(v -> showDatePickerDialog());

        // Handle save button click
        view.findViewById(R.id.saveButton).setOnClickListener(v -> saveProductDetails());
    }

    private void fetchCategories(AutoCompleteTextView categoryDropdown, AutoCompleteTextView subcategoryDropdown) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        apiService.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body();
                    List<String> categoryNames = new ArrayList<>();
                    Map<String, List<Subcategory>> subcategoryMap = new HashMap<>();

                    for (Category category : categories) {
                        categoryNames.add(category.getCategoryName());
                        subcategoryMap.put(category.getCategoryName(), category.getSubcategories());
                    }

                    ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_dropdown_item_1line, categoryNames);
                    categoryDropdown.setAdapter(categoryAdapter);

                    categoryDropdown.setOnItemClickListener((parent, view, position, id) -> {
                        String selectedCategory = categoryNames.get(position);
                        List<Subcategory> subcategories = subcategoryMap.get(selectedCategory);

                        if (subcategories != null) {
                            List<String> subcategoryNames = new ArrayList<>();
                            for (Subcategory sub : subcategories) {
                                subcategoryNames.add(sub.getSubCategoryName());
                            }

                            ArrayAdapter<String> subcategoryAdapter = new ArrayAdapter<>(getContext(),
                                    android.R.layout.simple_dropdown_item_1line, subcategoryNames);
                            subcategoryDropdown.setAdapter(subcategoryAdapter);
                        } else {
                            subcategoryDropdown.setAdapter(null);
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Failed to fetch categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(getContext(), "Error fetching categories: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchConditions(AutoCompleteTextView conditionDropdown) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        apiService.getConditionChoices().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> conditionList = response.body();
                    ArrayAdapter<String> conditionAdapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_dropdown_item_1line, conditionList);
                    conditionDropdown.setAdapter(conditionAdapter);
                } else {
                    Toast.makeText(getContext(), "Failed to fetch conditions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(getContext(), "Error fetching conditions: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePickerDialog() {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Show date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    // Format the date and set it in the EditText
                    String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    purchaseDateEditText.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveProductDetails() {
        // Collect all field values
        String assetName = assetNameEditText.getText().toString().trim();
        String purchaseDate = purchaseDateEditText.getText().toString().trim();
        String assetValue = assetValueEditText.getText().toString().trim();
        String condition = conditionDropdown.getText().toString().trim();
        String selectedCategory = categoryDropdown.getText().toString().trim();
        String selectedSubcategory = subcategoryDropdown.getText().toString().trim();

        // Validate inputs
        if (assetName.isEmpty() || purchaseDate.isEmpty() || assetValue.isEmpty() || condition.isEmpty() || selectedCategory.isEmpty() || selectedSubcategory.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create ProductDetails object with category and subcategory
        ProductDetails productDetails = new ProductDetails(scannedBarcode, assetName, purchaseDate, assetValue, condition, mergedLocation, selectedCategory, selectedSubcategory);

        // Initialize Retrofit and make the API call
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        apiService.saveProductDetails(productDetails).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Asset saved successfully!", Toast.LENGTH_SHORT).show();
                    navigateToProductList();
                } else {
                    Toast.makeText(getContext(), "Failed to save asset.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void navigateToProductList() {
        Fragment productlistaddFragment = new ProductListFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, productlistaddFragment)
                .addToBackStack(null)
                .commit();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(getContext(), "Location permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}