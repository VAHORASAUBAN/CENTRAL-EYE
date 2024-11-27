package com.example.integration.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.integration.R;
import com.example.integration.api.ApiService;
import com.example.integration.network.RetrofitClient;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductListFragment newInstance(String param1, String param2) {
        ProductListFragment fragment = new ProductListFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.productRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        TextView allTab = view.findViewById(R.id.all);
        TextView availableTab = view.findViewById(R.id.available);
        TextView inUseTab = view.findViewById(R.id.in_use);
        TextView maintainenceTab = view.findViewById(R.id.maintainence);
        TextView expiredTab = view.findViewById(R.id.expired);

        ImageView filterIcon = view.findViewById(R.id.filter);
        filterIcon.setOnClickListener(v -> showFilterDropdown(v));




        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Fetch all products from the API on fragment load
        apiService.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> allProducts = response.body();

                    // Initialize with all products
                    updateProductList(recyclerView, allProducts);

                    // Set click listeners for each tab
                    allTab.setOnClickListener(v -> {
                        highlightTab(allTab, availableTab, inUseTab, maintainenceTab,expiredTab);
                        updateProductList(recyclerView, allProducts); // Show all products
                    });

                    availableTab.setOnClickListener(v -> {
                        highlightTab(availableTab, allTab, inUseTab, maintainenceTab,expiredTab);
                        updateProductList(recyclerView, filterProductsByStatus(allProducts, "Available"));
                    });

                    inUseTab.setOnClickListener(v -> {
                        highlightTab(inUseTab, allTab, availableTab, maintainenceTab,expiredTab);
                        updateProductList(recyclerView, filterProductsByStatus(allProducts, "In-Use"));
                    });

                    maintainenceTab.setOnClickListener(v -> {
                        highlightTab(maintainenceTab, allTab, availableTab, inUseTab);
                        updateProductList(recyclerView, filterProductsByStatus(allProducts, "Maintenance"));
                    });
                        expiredTab.setOnClickListener(v -> {
                        highlightTab(expiredTab,maintainenceTab, allTab, availableTab, inUseTab);
                        updateProductList(recyclerView, filterProductsByStatus(allProducts, "expired"));
                    });


                    highlightTab(allTab, availableTab, inUseTab, maintainenceTab);
                    updateProductList(recyclerView, allProducts);
                } else {
                    Toast.makeText(getContext(), "Failed to load products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void navigateToProductDescription(Product product) {
        // Create a new instance of ProductDescriptionFragment with arguments
        ProductDescriptionFragment fragment = ProductDescriptionFragment.newInstance(
                product.getLocation(), product.getBarcode()
        );

        // Start a transaction to replace the fragment
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment) // Ensure this ID matches your container
                .addToBackStack(null)
                .commit();
    }

    private void updateProductList(RecyclerView recyclerView, List<Product> productList) {
        ProductAdapter adapter = new ProductAdapter(getContext(), productList, product -> {
            // Handle item click
            navigateToProductDescription(product);
        });
        recyclerView.setAdapter(adapter);
    }
    /**
     * Filters the product list based on the status.
     */
    private List<Product> filterProductsByStatus(List<Product> products, String status) {
        return products.stream()
//                .filter(product -> status.equalsIgnoreCase(product.getStatus()))
                .collect(Collectors.toList());
    }


    private void showFilterDropdown(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        // Inflate the menu layout
        popupMenu.getMenuInflater().inflate(R.menu.filter_menu, popupMenu.getMenu());

        // Set menu item click listeners
        popupMenu.setOnMenuItemClickListener(item -> {
            int id=item.getItemId();
            if (id == R.id.filter_peripheral) {
                // Handle "Peripheral" filter
                Toast.makeText(getContext(), "Filter: Peripheral", Toast.LENGTH_SHORT).show();
                // Update product list based on filter...
                return true;

            } else if (id == R.id.filter_CPU) {
                // Handle "CPU" filter
                Toast.makeText(getContext(), "Filter: CPU", Toast.LENGTH_SHORT).show();
                // Update product list based on filter...
                return true;

            } else if (id == R.id.filter_Monitor) {
                // Handle "Monitor" filter
                Toast.makeText(getContext(), "Filter: Monitor", Toast.LENGTH_SHORT).show();
                // Update product list based on filter...
                return true;

            } else if (id == R.id.filter_stationary) {
                // Handle "Stationary" filter
                Toast.makeText(getContext(), "Filter: Stationary", Toast.LENGTH_SHORT).show();
                // Update product list based on filter...
                return true;

            } else if (id == R.id.filter_Pencil) {
                // Handle "Pencil" filter
                Toast.makeText(getContext(), "Filter: Pencil", Toast.LENGTH_SHORT).show();
                // Update product list based on filter...
                return true;

            } else {
                return false;
            }
        });

        // Show the menu
        popupMenu.show();
    }
    private void highlightTab(TextView selectedTab, TextView... otherTabs) {
        // Highlight the selected tab
        selectedTab.setBackgroundResource(R.drawable.tab_background_selected); // Use rounded corner drawable
        selectedTab.setTextColor(getResources().getColor(R.color.white));

        // Reset other tabs
        for (TextView tab : otherTabs) {
            tab.setBackgroundResource(R.drawable.tab_background_unselected); // Use white background
            tab.setTextColor(getResources().getColor(R.color.black));
        }
    }

}