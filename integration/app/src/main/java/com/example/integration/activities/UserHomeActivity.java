package com.example.integration.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Button;

import com.example.integration.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserHomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    User_Home_fragment user_homeFragment = new User_Home_fragment();
    User_Profile_fragment user_profileFragment = new User_Profile_fragment();
    User_Add_Product_Scanner user_addProductScanner = User_Add_Product_Scanner.newInstance();
    private SharedPreferences sharedPreferences;
    private TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

        // Fetch the username from session
        String username = sharedPreferences.getString("username", "User");

        // Bind views
        bottomNavigationView = findViewById(R.id.bottom_navigation);
//        Button logoutButton = findViewById(R.id.logoutButton);

        // Set welcome message

        // Default fragment (Home)
        replaceFragmentWithAnimation(user_homeFragment, R.anim.fade_in, R.anim.fade_out);

        // Set listener for BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                View selectedView = bottomNavigationView.findViewById(item.getItemId()); // Get the selected item view
                playUpsideAnimation(selectedView); // Add animation effect

                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    replaceFragmentWithAnimation(user_homeFragment, R.anim.slide_in_left, R.anim.slide_out_right);
                    return true;
                } else if (id == R.id.nav_products) {
                    replaceFragmentWithAnimation(user_addProductScanner, R.anim.fade_in, R.anim.fade_out);
                    return true;
                } else if (id == R.id.nav_profile) {
                    replaceFragmentWithAnimation(user_profileFragment, R.anim.slide_in_left, R.anim.slide_out_right);
                    return true;
                }
                return false;
            }
        });

        // Logout functionality
//        logoutButton.setOnClickListener(v -> {
//            // Clear the session
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.clear();
//            editor.apply();
//
//            // Redirect to login screen
//            Intent intent = new Intent(UserHomeActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        });
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