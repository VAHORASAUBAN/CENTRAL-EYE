package com.example.integration.activities;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.integration.R;
import com.example.integration.api.ApiService;
import com.example.integration.network.RetrofitClient;
import com.example.integration.api.AssignProduct;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_Scanner_Form_DetailsFragment extends Fragment {

    private static final String ARG_SCANNED_BARCODE = "scannedBarcode";
    private EditText returnDateEditText;
    private String scannedBarcode;
    private TextView barcodeTextView;
    private SharedPreferences sharedPreferences;

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
        // Initialize SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences("UserSession", getActivity().MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scanner__form__details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        barcodeTextView = view.findViewById(R.id.barcodeTextView);
        returnDateEditText = view.findViewById(R.id.returnDate);

        // Set the scanned barcode
        if (scannedBarcode != null) {
            barcodeTextView.setText(scannedBarcode);
        }

        // Handle date picker for purchaseDateEditText
        returnDateEditText.setOnClickListener(v -> showDatePickerDialog());

        // Handle save button click
        view.findViewById(R.id.saveButton).setOnClickListener(v -> saveAssignProduct());
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
                    returnDateEditText.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveAssignProduct() {
        // Collect all field values
        String returnDate = returnDateEditText.getText().toString().trim();

        // Validate inputs
        if (returnDate.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve session details from SharedPreferences
        String sessionId = sharedPreferences.getString("session_id", "");
        String username = sharedPreferences.getString("username", "");

        // Create AssignProduct object
        AssignProduct assignProduct = new AssignProduct(scannedBarcode, returnDate, username);

        // Initialize Retrofit and make the API call
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        apiService.saveAssignProduct(assignProduct).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Asset saved successfully!", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
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
}
