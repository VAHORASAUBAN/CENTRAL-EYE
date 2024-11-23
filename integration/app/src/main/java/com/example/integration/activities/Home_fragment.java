package com.example.integration.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.integration.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home_fragment extends Fragment {

    private TextView headText;
    private ImageButton logoutButton;
    private SharedPreferences sharedPreferences;

    public Home_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_fragment, container, false);

        // Initialize views
        headText = view.findViewById(R.id.headtext);
        logoutButton = view.findViewById(R.id.logoutButtonUser);
        if (logoutButton == null) {
            Log.e("Logout", "Logout button is null!");
        } else {
            Log.d("Logout", "Logout button initialized successfully.");
        }
        // Fetch session details
        sharedPreferences = requireActivity().getSharedPreferences("UserSession", getContext().MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "User");

        // Set username to headtext
        headText.setText(username + "!");

        // Set up logout functionality
        logoutButton.setOnClickListener(v -> {
            Log.d("Logout", "Logout button clicked");

            // Clear session and redirect
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Log.d("Logout", "Session cleared");

            Intent intent = new Intent(requireActivity(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        setupUIInteractions(view);

        return view;
    }

    private void setupUIInteractions(View view) {
        LinearLayout squareBox1 = view.findViewById(R.id.squareBox1);

        // Add click listener with animation to squareBox1
        squareBox1.setOnClickListener(v -> {
            v.animate()
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .setDuration(100)
                    .withEndAction(() -> {
                        v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                        openProductListFragment();
                    })
                    .start();
        });
    }

    private void openProductListFragment() {
        ProductListFragment productListFragment = new ProductListFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, productListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
