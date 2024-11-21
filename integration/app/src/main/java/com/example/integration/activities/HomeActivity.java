package com.example.integration.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.integration.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Home_fragment homeFragment = new Home_fragment();
    Profile_fragment profileFragment = new Profile_fragment();
    Add_Product_Scanner addProductScanner = new Add_Product_Scanner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Default fragment (Home)
        replaceFragmentWithAnimation(homeFragment, R.anim.fade_in, R.anim.fade_out);

        // Set listener for BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                View selectedView = bottomNavigationView.findViewById(item.getItemId()); // Get the selected item view
                playUpsideAnimation(selectedView); // Add animation effect

                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    replaceFragmentWithAnimation(homeFragment, R.anim.slide_in_left, R.anim.slide_out_right);
                    return true;
                } else if (id == R.id.nav_products) {
                    replaceFragmentWithAnimation(addProductScanner, R.anim.fade_in, R.anim.fade_out);
                    return true;
                } else if (id == R.id.nav_profile) {
                    replaceFragmentWithAnimation(profileFragment, R.anim.slide_in_left, R.anim.slide_out_right);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Function to replace fragments with animations.
     */
    private void replaceFragmentWithAnimation(androidx.fragment.app.Fragment fragment, int enterAnim, int exitAnim) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(enterAnim, exitAnim);
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
