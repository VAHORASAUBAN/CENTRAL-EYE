package com.example.integration.activities.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.integration.R;
import com.example.integration.activities.adapter.RequestItemAdapter;
import com.example.integration.activities.model.RequestItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Request_details#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Request_details extends Fragment {

    private RecyclerView itemRecyclerView;
    private RequestItemAdapter adapter;
    private List<RequestItemModel> itemList;

    private static final String ARG_REQUEST_ID = "requestId";
    private String requestId;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Request_details() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Request_details newInstance(String requestId) {
        Request_details fragment = new Request_details();
        Bundle args = new Bundle();
        args.putString(ARG_REQUEST_ID, requestId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            requestId = getArguments().getString(ARG_REQUEST_ID); // Get the request ID
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_details, container, false);

        itemRecyclerView = view.findViewById(R.id.itemRecyclerView);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch or prepare details for the specific request
        loadRequestDetails();

        return view;
    }

    private void loadRequestDetails() {
        // Sample data based on requestId
        itemList = new ArrayList<>();
        if ("REQ001".equals(requestId)) {
            itemList.add(new RequestItemModel("Laptop", 10, 5, "2024-12-31"));
            itemList.add(new RequestItemModel("Mouse", 20, 15, "2024-12-20"));
        } else if ("REQ002".equals(requestId)) {
            itemList.add(new RequestItemModel("Keyboard", 5, 3, "2024-11-15"));
            itemList.add(new RequestItemModel("Monitor", 8, 6, "2024-11-20"));
        }

        // Pass context to the adapter
        adapter = new RequestItemAdapter(getContext(), itemList);
        itemRecyclerView.setAdapter(adapter);
    }

}