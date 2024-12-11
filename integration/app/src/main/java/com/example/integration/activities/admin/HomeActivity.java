package com.example.integration.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.integration.R;
import com.example.integration.activities.Profile_fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fab;

    private final Fragment homeFragment = new Home_fragment();
    private final Fragment profileFragment = new Profile_fragment();
    private final Fragment adminNotificationFragment = new Adminnotification();

    private final Fragment addProductScanner = new Add_Product_Scanner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fab = findViewById(R.id.nav_products);

        // Default fragment (Home)
        replaceFragment(homeFragment);

        // Handle BottomNavigationView item selection
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);

        // Handle FloatingActionButton click
        fab.setOnClickListener(v -> {
            // Navigate to the Add Product Scanner fragment
            replaceFragment(addProductScanner);
        });
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        View selectedView = bottomNavigationView.findViewById(item.getItemId()); // Get the selected item view
        playUpsideAnimation(selectedView); // Add animation effect

        int id = item.getItemId();
        if (id == R.id.nav_home) {
            replaceFragment(homeFragment);
        } else if (id == R.id.nav_products) {
            replaceFragment(addProductScanner);
        } else if (id == R.id.nav_notify) {
            replaceFragment(adminNotificationFragment);
        }
        return true;
    }

    /**
     * Function to replace fragments without animation.
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    /**
     * Play animation for the selected BottomNavigationView item.
     */
    private void playUpsideAnimation(View selectedView) {
        if (selectedView != null) {
            // Load bounce animation
            Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
            selectedView.startAnimation(bounce);

            // Simulate a "pop-out" effect
            selectedView.animate()
                    .scaleX(1.2f) // Increase the size horizontally
                    .scaleY(1.2f) // Increase the size vertically
                    .translationY(-20f) // Move the item upwards by 20dp
                    .setDuration(200)
                    .withEndAction(() -> selectedView.animate()
                            .scaleX(1.0f) // Return to normal size horizontally
                            .scaleY(1.0f) // Return to normal size vertically
                            .translationY(0f) // Return to original position
                            .setDuration(200)
                            .start())
                    .start();
        }
    }
}