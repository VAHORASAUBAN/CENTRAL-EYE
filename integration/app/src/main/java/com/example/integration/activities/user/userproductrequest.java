package com.example.integration.activities.user;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.integration.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link userproductrequest#newInstance} factory method to
 * create an instance of this fragment.
 */
public class userproductrequest extends Fragment {

    private LinearLayout formsContainer;
    private Button requestMoreCategoryButton, requestButton;
    private HashMap<String, List<String>> subCategoryMap;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public userproductrequest() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment userproductrequest.
     */
    // TODO: Rename and change types and number of parameters
    public static userproductrequest newInstance(String param1, String param2) {
        userproductrequest fragment = new userproductrequest();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_userproductrequest, container, false);




        requestMoreCategoryButton = view.findViewById(R.id.requestMoreCategoryButton);
        requestButton = view.findViewById(R.id.requestButton);
        formsContainer = view.findViewById(R.id.formsContainer);
        setupCategoryData();

        addNewForm();


        requestButton.setOnClickListener(v -> processAllForms());
        requestMoreCategoryButton.setOnClickListener(v -> addNewForm());


        return view;
    }

    private void openDatePicker(EditText returnDateEditText) {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Update the EditText with the selected date
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    returnDateEditText.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }
    private void setupCategoryData() {
        // Initialize category and subcategory data
        subCategoryMap = new HashMap<>();
        subCategoryMap.put("Electronics", List.of("Mobile", "Laptop", "Tablet"));
        subCategoryMap.put("Fashion", List.of("Clothing", "Shoes", "Accessories"));
        subCategoryMap.put("Home Appliances", List.of("Refrigerator", "Washing Machine", "Microwave"));
        subCategoryMap.put("Books", List.of("Fiction", "Non-fiction", "Comics"));
    }

    private void addNewForm() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View formView = inflater.inflate(R.layout.dynamic_form_layout, formsContainer, false);

        Spinner categorySpinner = formView.findViewById(R.id.categorySpinner);
        Spinner subCategorySpinner = formView.findViewById(R.id.subCategorySpinner);
        TextView quantityInput = formView.findViewById(R.id.quantityDisplay);
        quantityInput.setText("0");
        EditText returnDateEditText = formView.findViewById(R.id.returndate);
        returnDateEditText.setText("");
        returnDateEditText.setOnClickListener(v -> openDatePicker(returnDateEditText));

        Button incrementbtn = formView.findViewById(R.id.incrementbtn);
        Button decrementbtn = formView.findViewById(R.id.decrementbtn);

        // Set up the category spinner
        List<String> categories = new ArrayList<>(subCategoryMap.keySet());
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Handle category spinner item selection
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categories.get(position);
                populateSubCategorySpinner(subCategorySpinner, selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection if needed
            }
        });

        // Quantity increment and decrement logic
        incrementbtn.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(quantityInput.getText().toString());
            quantityInput.setText(String.valueOf(currentQuantity + 1));
        });

        decrementbtn.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(quantityInput.getText().toString());
            if (currentQuantity > 0) {
                quantityInput.setText(String.valueOf(currentQuantity - 1));
            } else {
                Toast.makeText(requireContext(), "Quantity cannot be less than 0", Toast.LENGTH_SHORT).show();
            }
        });

        formsContainer.addView(formView);
    }


    private void populateSubCategorySpinner(Spinner subCategorySpinner, String category) {
        List<String> subCategories = subCategoryMap.get(category);

        if (subCategories != null) {
            ArrayAdapter<String> subCategoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, subCategories);
            subCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            subCategorySpinner.setAdapter(subCategoryAdapter);

            subCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedSubCategory = subCategories.get(position);
                    Toast.makeText(requireContext(), "Selected Subcategory: " + selectedSubCategory, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Handle no selection if needed
                }
            });
        }
    }

    private void processAllForms() {
        List<HashMap<String, String>> formDataList = new ArrayList<>();

        for (int i = 0; i < formsContainer.getChildCount(); i++) {
            View formView = formsContainer.getChildAt(i);

            Spinner categorySpinner = formView.findViewById(R.id.categorySpinner);
            Spinner subCategorySpinner = formView.findViewById(R.id.subCategorySpinner);
            TextView quantityInput = formView.findViewById(R.id.quantityDisplay);

            String category = categorySpinner.getSelectedItem().toString();
            String subCategory = subCategorySpinner.getSelectedItem().toString();
            String quantity = quantityInput.getText().toString();

            if (quantity.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill out all fields in form " + (i + 1), Toast.LENGTH_SHORT).show();
                return;
            }

            HashMap<String, String> formData = new HashMap<>();
            formData.put("category", category);
            formData.put("subCategory", subCategory);
            formData.put("quantity", quantity);

            formDataList.add(formData);
        }
    }

}