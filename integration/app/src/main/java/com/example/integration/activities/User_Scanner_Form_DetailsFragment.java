package com.example.integration.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.fragment.app.Fragment;

import com.example.integration.R;
import com.example.integration.api.ApiService;
import com.example.integration.api.AssignProduct;
import com.example.integration.network.RetrofitClient;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_Scanner_Form_DetailsFragment extends Fragment {

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
        sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user__scanner__form__details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        barcodeTextView = view.findViewById(R.id.barcodeTextView);
        returnDateEditText = view.findViewById(R.id.returnDateEditText);

        // Display scanned barcode
        if (scannedBarcode != null) {
            barcodeTextView.setText("Barcode: " + scannedBarcode);
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
        String username = sharedPreferences.getString("username", "");

        // Validate inputs
        if (returnDate.isEmpty()) {
            Toast.makeText(getContext(), "Please select a return date.", Toast.LENGTH_SHORT).show();
            return;
        }
        

        AssignProduct assignProduct = new AssignProduct(scannedBarcode, returnDate, username);
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Make API call
        apiService.saveAssignProduct(assignProduct).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Product assigned successfully!", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Log.e("API Error", "Response error: " + response.errorBody());
                    Toast.makeText(getContext(), "Failed to assign product.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API Failure", "Error: " + t.getMessage());
                Toast.makeText(getContext(), "Failed to assign product. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
