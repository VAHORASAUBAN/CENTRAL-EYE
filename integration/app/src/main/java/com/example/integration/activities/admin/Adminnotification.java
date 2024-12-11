package com.example.integration.activities.admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.integration.R;
import com.example.integration.activities.MainActivity;
import com.example.integration.activities.SearchScanner;
import com.example.integration.activities.model.Notification;
import com.example.integration.activities.adapter.ProductNotificationAdapter;
import com.example.integration.activities.adapter.RequestListAdapter;
import com.example.integration.activities.model.RequestModel;
import com.example.integration.activities.user.User_Profile_fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Adminnotification#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Adminnotification extends Fragment {


    private RecyclerView requestRecyclerView;
    private RequestListAdapter requestAdapter;
    private ProductNotificationAdapter productNotificationAdapter;
    private TextView newRequestTab;
    private TextView productInfoTab;
    private List<RequestModel> requestList;
    private ImageButton flashlightButton;
    private boolean isFlashlightOn = false;
    private List<Notification> notificationList;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Adminnotification() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Adminnotification.
     */
    // TODO: Rename and change types and number of parameters
    public static Adminnotification newInstance(String param1, String param2) {
        Adminnotification fragment = new Adminnotification();
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
        View view = inflater.inflate(R.layout.fragment_adminnotification, container, false);

        // Initialize views
        ImageView profileImageButton = view.findViewById(R.id.profile_image);
        ImageButton scanner_icon = view.findViewById(R.id.scanner_icon);
        requestRecyclerView = view.findViewById(R.id.requestRecyclerView);
        newRequestTab = view.findViewById(R.id.newrequest);
        productInfoTab = view.findViewById(R.id.Product_information);

        // Set up RecyclerView with default layout manager
        requestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));




        // Set up RecyclerViews
        requestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        productRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Sample data for Request RecyclerView
        requestList = new ArrayList<>();
        requestList.add(new RequestModel("REQ001", "John Doe"));
        requestList.add(new RequestModel("REQ002", "Jane Smith"));
        requestAdapter = new RequestListAdapter(getParentFragmentManager(), requestList);
        requestRecyclerView.setAdapter(requestAdapter);

        // Sample data for Product Notifications
        notificationList = new ArrayList<>();
        notificationList.add(new Notification("Reminder", "Your Asset with ID: 12321 is due on 21/7/2024"));
        notificationList.add(new Notification("Alert", "Your Asset with ID: 45678 is overdue since 15/6/2024"));
        productNotificationAdapter = new ProductNotificationAdapter(notificationList, new ProductNotificationAdapter.OnNotificationClickListener() {
            @Override
            public void onNotificationClick(Notification notification) {
                // Handle the click event here (can be left empty if not needed)
            }
        });
        // Set default tab as New Request
        highlightTab(newRequestTab, productInfoTab);
        showNewRequestRecyclerView();

        // Tab click listeners
        newRequestTab.setOnClickListener(v -> {
            highlightTab(newRequestTab, productInfoTab);
            showNewRequestRecyclerView();
        });

        productInfoTab.setOnClickListener(v -> {
            highlightTab(productInfoTab, newRequestTab);
            showProductInfoRecyclerView(notificationList);
        });

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
        return view;
    }

    private void showNewRequestRecyclerView() {
        // Show New Request RecyclerView
        requestRecyclerView.setVisibility(View.VISIBLE);
        if (productNotificationAdapter != null) {
            requestRecyclerView.setAdapter(requestAdapter);
        }
    }

    private void showProductInfoRecyclerView(List<Notification> notificationList) {
        // Show Product Information RecyclerView
        requestRecyclerView.setVisibility(View.VISIBLE);
        if (productNotificationAdapter != null) {
            requestRecyclerView.setAdapter(productNotificationAdapter);
        }
    }

    private void highlightTab(TextView selectedTab, TextView otherTab) {
        // Highlight selected tab
        selectedTab.setBackgroundResource(R.drawable.tab_background_selected); // Replace with actual drawable
        selectedTab.setTextColor(getResources().getColor(R.color.white)); // Adjust colors

        // Reset the unselected tab
        otherTab.setBackgroundResource(R.drawable.tab_background_unselected); // Replace with actual drawable
        otherTab.setTextColor(getResources().getColor(R.color.black)); // Adjust colors
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


}