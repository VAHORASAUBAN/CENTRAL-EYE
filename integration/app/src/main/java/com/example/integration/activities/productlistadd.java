package com.example.integration.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.integration.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link productlistadd#newInstance} factory method to
 * create an instance of this fragment.
 */
public class productlistadd extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public productlistadd() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment productlistadd.
     */
    // TODO: Rename and change types and number of parameters
    public static productlistadd newInstance(String param1, String param2) {
        productlistadd fragment = new productlistadd();
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
// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_productlistadd, container, false);

        // Handle button click
        view.findViewById(R.id.viewButton).setOnClickListener(v -> onViewButtonClick());

        return view;
    }
    // Method to handle button click and switch to ProductDescriptionFragment
    private void onViewButtonClick() {
        // Create a new instance of ProductDescriptionFragment
        ProductDescriptionFragment productDescriptionFragment = new ProductDescriptionFragment();

        // Start a transaction and replace the current fragment
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new ProductDescriptionFragment()) // Make sure to use your actual container ID
                .addToBackStack(null) // Optional: add to back stack if you want to return to this fragment
                .commit();
    }
}