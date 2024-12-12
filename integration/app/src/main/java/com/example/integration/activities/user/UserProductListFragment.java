package com.example.integration.activities.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.integration.R;
import com.example.integration.activities.MainActivity;
import com.example.integration.activities.ProductDescriptionFragment;
import com.example.integration.activities.SearchScanner;
import com.example.integration.activities.adapter.ProductAdapter;
import com.example.integration.activities.model.Category;
import com.example.integration.activities.model.Product;
import com.example.integration.activities.model.Subcategory;
import com.example.integration.activities.user.User_Profile_fragment;
import com.example.integration.api.ApiService;
import com.example.integration.network.RetrofitClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProductListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProductListFragment extends Fragment {
    private RecyclerView recyclerView;
    private CardView filterIcon;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public UserProductListFragment() {
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
    public static UserProductListFragment newInstance(String param1, String param2) {
        UserProductListFragment fragment = new UserProductListFragment();
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
        View view = inflater.inflate(R.layout.fragment_user_product_list, container, false);

        recyclerView = view.findViewById(R.id.productRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ImageView profileImageButton = view.findViewById(R.id.profile_image);
        ImageButton scanner_icon = view.findViewById(R.id.scanner_icon);

        TextView inUseTab = view.findViewById(R.id.in_use);
        TextView duesoonTab = view.findViewById(R.id.duesoon);
        TextView returnTab = view.findViewById(R.id.returned);

        filterIcon = view.findViewById(R.id.filter);

        // Fetch products, categories, and subcategories via API
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        filterIcon.setOnClickListener(v -> {
            fetchCategories();  // Fetch categories when the filter icon is clicked
        });

        scanner_icon.setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new SearchScanner())
                    .addToBackStack(null)
                    .commit();
        });

        profileImageButton.setOnClickListener(v -> {
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

        // Fetch all products from API

        String username = getUsername(); // Retrieve username
        apiService.getUserProducts(username).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> allProducts = response.body();
                    updateProductList(recyclerView, allProducts);

                    inUseTab.setOnClickListener(v -> {
                        highlightTab(inUseTab, duesoonTab, returnTab);
                        updateProductList(recyclerView, allProducts); // Show all products
                    });

                    duesoonTab.setOnClickListener(v -> {
                        highlightTab(duesoonTab, inUseTab, returnTab);
                        fetchFilteredProducts(recyclerView, duesoonTab.getText().toString());
                    });
                    returnTab.setOnClickListener(v -> {
                        highlightTab(returnTab, duesoonTab, inUseTab);
                        fetchFilteredProducts(recyclerView, returnTab.getText().toString());
                    });

                    highlightTab(inUseTab, duesoonTab, returnTab);
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

    private String getUsername() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "user");
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

    private void fetchCategories() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        apiService.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body();
                    showCategoryMenu(categories); // Show dynamic categories in the menu
                } else {
                    Toast.makeText(getContext(), "Failed to load categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showCategoryMenu(List<Category> categories) {
        PopupMenu categoryMenu = new PopupMenu(getContext(), filterIcon); // Attach to the filter icon
        for (Category category : categories) {
            categoryMenu.getMenu().add(category.getCategoryName()); // Use category names from API
        }

        categoryMenu.setOnMenuItemClickListener(item -> {
            String selectedCategory = item.getTitle().toString();
            int categoryId = findCategoryIdByName(categories, selectedCategory); // Get category ID by name
            fetchSubcategories(categoryId); // Fetch subcategories for selected category
            return true;
        });

        categoryMenu.show();
    }

    private int findCategoryIdByName(List<Category> categories, String categoryName) {
        for (Category category : categories) {
            if (category.getCategoryName().equals(categoryName)) {
                return category.getCategoryId(); // Assuming Category has `id` and `name` properties
            }
        }
        return -1; // Return an invalid ID if not found
    }

    private void fetchSubcategories(int categoryId) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        apiService.getSubcategories(categoryId).enqueue(new Callback<List<Subcategory>>() {
            @Override
            public void onResponse(Call<List<Subcategory>> call, Response<List<Subcategory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Subcategory> subcategories = response.body();
                    showSubcategoryMenu(subcategories); // Show dynamic subcategories in the menu
                } else {
                    Toast.makeText(getContext(), "Failed to load subcategories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Subcategory>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSubcategoryMenu(List<Subcategory> subcategories) {
        PopupMenu subcategoryMenu = new PopupMenu(getContext(), filterIcon); // Attach to the filter icon
        for (Subcategory subcategory : subcategories) {
            subcategoryMenu.getMenu().add(subcategory.getSubCategoryName()); // Use subcategory names from API
        }

        subcategoryMenu.setOnMenuItemClickListener(item -> {
            String selectedSubcategory = item.getTitle().toString();
            // Handle subcategory selection here, for example, filter products
            Toast.makeText(getContext(), "Selected Subcategory: " + selectedSubcategory, Toast.LENGTH_SHORT).show();
            return true;
        });

        subcategoryMenu.show();
    }

    private void fetchFilteredProducts(RecyclerView recyclerView, String filterType) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        String username = getUsername(); // Retrieve username
        apiService.getUserProductsWithFilter(username, filterType).enqueue(new Callback<List<Product>>() {
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

    private void showSubcategoryMenu(String category, List<String> subcategoryList) {
        PopupMenu subcategoryMenu = new PopupMenu(getContext(), getView().findViewById(R.id.filter));
        for (String subcategory : subcategoryList) {
            subcategoryMenu.getMenu().add(subcategory);
        }

        subcategoryMenu.setOnMenuItemClickListener(item -> {
            String selectedSubcategory = item.getTitle().toString();

            // Update product list based on selected category and subcategory
            updateProductListByCategoryAndSubcategory(category, selectedSubcategory);
            return true;
        });

        subcategoryMenu.show();
    }

    private void updateProductListByCategoryAndSubcategory(String category, String subcategory) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        apiService.getProductsByCategoryAndSubcategory(category, subcategory).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> filteredProducts = response.body();
                    updateProductList(recyclerView, filteredProducts); // Update the RecyclerView with filtered products
                    Toast.makeText(getContext(), "Filtered by " + category + " > " + subcategory, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "No products found in this category and subcategory", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToProductDescription(Product product) {
        ProductDescriptionFragment fragment = ProductDescriptionFragment.newInstance(product);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
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