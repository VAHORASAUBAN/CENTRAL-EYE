package com.example.integration.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.integration.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity  {
    BottomNavigationView bottomNavigationView;
    Home_fragment homeFragment = new Home_fragment();

    Profile_fragment profileFragment = new Profile_fragment();

    Product_fragment product_fragment = new Product_fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        final int nav_home = R.id.nav_home;
        final int nav_products = R.id.nav_products;
        final int nav_profile = R.id.nav_profile;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == nav_home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
                    return true;
                } else if (item.getItemId() == nav_products) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, product_fragment).commit();
                    return true;
                } else if (item.getItemId() == nav_profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,profileFragment).commit();
                    return true;
                }
                return false;
            }
        });
    }
}
