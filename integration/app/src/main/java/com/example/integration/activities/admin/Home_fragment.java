package com.example.integration.activities.admin;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.integration.R;
import com.example.integration.activities.MainActivity;
import com.example.integration.activities.ProductDescriptionFragment;
import com.example.integration.activities.ProductListFragment;
import com.example.integration.activities.SearchScanner;
import com.example.integration.activities.adapter.ProductAdapter;
import com.example.integration.activities.model.Product;
import com.example.integration.activities.user.User_Profile_fragment;
import com.example.integration.api.ApiService;
import com.example.integration.api.TotalsResponse;
import com.example.integration.network.RetrofitClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home_fragment extends Fragment {

    private static final int REQUEST_CHECK_SETTINGS = 1001;
    private TextView totalProductsTextView;
    private TextView totalUsersTextView;


    public Home_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_fragment, container, false);

        // Initialize UI elements
        totalProductsTextView = view.findViewById(R.id.total_products);
        totalUsersTextView = view.findViewById(R.id.total_users);
        RecyclerView recyclerView = view.findViewById(R.id.productRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fetchFilteredProducts(recyclerView, "barcode-remaining");



        // Fetch totals from the backend
        fetchTotals();

        // Bind views
        ImageView profileImageButton = view.findViewById(R.id.profile_image);
        TextView viewall = view.findViewById(R.id.viewall);

        CardView squareBox1 = view.findViewById(R.id.squareBox1);
        CardView squareBox2 = view.findViewById(R.id.squareBox2);

        ImageButton scanner_icon = view.findViewById(R.id.scanner_icon);


        scanner_icon.setOnClickListener(v -> {
            // Navigate to ProductListAddFragment
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new SearchScanner())
                    .addToBackStack(null) // Optional, adds transaction to back stack
                    .commit();
        });

        // Profile dropdown menu
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
        viewall.setOnClickListener(v -> {
            ProductListFragment productListFragment = ProductListFragment.newInstance("barcode-remaining", null);
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, productListFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        });


        // Add click listeners to the card views
        squareBox1.setOnClickListener(v -> openProductListFragment());
        squareBox2.setOnClickListener(v -> openUserListAdd());

        // Check location settings
        checkLocationSettings();

        return view;
    }

    private void fetchTotals() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<TotalsResponse> call = apiService.getTotals();

        call.enqueue(new Callback<TotalsResponse>() {
            @Override
            public void onResponse(Call<TotalsResponse> call, Response<TotalsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TotalsResponse totals = response.body();
                    totalProductsTextView.setText(String.valueOf(totals.getTotalProducts()));
                    totalUsersTextView.setText(String.valueOf(totals.getTotalUsers()));
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TotalsResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

    private void openProductListFragment() {
        ProductListFragment productListFragment = new ProductListFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, productListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openUserListAdd() {
        UserListAdd userListFragment = new UserListAdd();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, userListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void fetchFilteredProducts(RecyclerView recyclerView, String filterType) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Fetch products with the filter query
        apiService.getProductsWithFilter(filterType).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> filteredProducts = response.body();
                    updateProductList(recyclerView, filteredProducts); // Update the RecyclerView with filtered products
                } else {
                    Toast.makeText(getContext(), "Failed to load products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProductList(RecyclerView recyclerView, List<Product> productList) {
        ProductAdapter adapter = new ProductAdapter(getContext(), productList, product -> {
            // Handle item click
            navigateToProductDescription(product);
        });
        recyclerView.setAdapter(adapter);
    }
    private void navigateToProductDescription(Product product) {
        ProductDescriptionFragment fragment = ProductDescriptionFragment.newInstance(product);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
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
}