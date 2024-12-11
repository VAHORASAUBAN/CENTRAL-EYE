package com.example.integration.activities.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.integration.R;
import com.example.integration.activities.model.Notification;
import com.example.integration.activities.adapter.ProductNotificationAdapter;
import com.example.integration.activities.adapter.RequestListAdapter;
import com.example.integration.activities.model.RequestModel;
import com.example.integration.api.ApiService;
import com.example.integration.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adminnotification, container, false);

        // Initialize views
        requestRecyclerView = view.findViewById(R.id.requestRecyclerView);
        newRequestTab = view.findViewById(R.id.newrequest);
        productInfoTab = view.findViewById(R.id.Product_information);

        // Set up RecyclerView with default layout manager
        requestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch requests from API
        fetchRequestsFromAPI();

        // Set default tab as New Request
        highlightTab(newRequestTab, productInfoTab);

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

    private void fetchRequestsFromAPI() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<RequestModel>> call = apiService.getRequests();

        call.enqueue(new Callback<List<RequestModel>>() {
            @Override
            public void onResponse(Call<List<RequestModel>> call, Response<List<RequestModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    requestList = response.body();
                    requestAdapter = new RequestListAdapter(getParentFragmentManager(), requestList);
                    requestRecyclerView.setAdapter(requestAdapter);
                } else {
                    // Handle the error
                    Toast.makeText(getContext(), "Failed to fetch requests.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<RequestModel>> call, Throwable t) {
                // Handle API failure
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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