package com.example.integration.activities.user;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.integration.R;
import com.example.integration.activities.adapter.ProductNotificationAdapter;
import com.example.integration.activities.adapter.RequestListAdapter;
import com.example.integration.activities.model.Notification;
import com.example.integration.activities.model.RequestModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link User_notification#newInstance} factory method to
 * create an instance of this fragment.
 */
public class User_notification extends Fragment {

    private RecyclerView requestRecyclerView;
    private RequestListAdapter requestAdapter;
    private ProductNotificationAdapter productNotificationAdapter;
    private TextView newRequestTab;
    private TextView productInfoTab;
    private List<RequestModel> requestList;
    private List<Notification> notificationList;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public User_notification() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment User_notification.
     */
    // TODO: Rename and change types and number of parameters
    public static User_notification newInstance(String param1, String param2) {
        User_notification fragment = new User_notification();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_notification, container, false);
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
//        requestList.add(new RequestModel("REQ001", "John Doe"));
//        requestList.add(new RequestModel("REQ002", "Jane Smith"));
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






}