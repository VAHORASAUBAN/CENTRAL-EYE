package com.example.integration.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.integration.activities.admin.HomeActivity;
import com.example.integration.activities.user.UserHomeActivity;
import com.example.integration.api.ApiService;
import com.example.integration.api.LoginRequest;
import com.example.integration.api.LoginResponse;
import com.example.integration.network.RetrofitClient;
import com.example.integration.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.primary));

        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);

        // Check if permissions are granted
        if (!checkPermissions()) {
            requestPermissions();
        }

        // Check if user is already logged in
        if (sharedPreferences.contains("username")) {
            navigateToHomeScreen(sharedPreferences.getString("role", ""), sharedPreferences.getString("username", ""));
            finish();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermissions()) {
                    Toast.makeText(MainActivity.this, "Permissions are required to proceed!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Both fields are required!", Toast.LENGTH_SHORT).show();
                } else {
                    // Create the login request model (send password in plain text)
                    LoginRequest loginRequest = new LoginRequest(username, password);

                    // Make the API call using Retrofit
                    Retrofit retrofit = RetrofitClient.getRetrofitInstance();
                    ApiService apiService = retrofit.create(ApiService.class);
                    Call<LoginResponse> call = apiService.login(loginRequest);

                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.isSuccessful()) {
                                LoginResponse loginResponse = response.body();
                                String role = loginResponse.getRole();  // Get the role from the response
                                String username = loginResponse.getUsername();

                                // Save user session
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", username);
                                editor.putString("role", role);
                                editor.apply();

                                // Navigate to home screen
                                navigateToHomeScreen(role, username);
                                finish();
                            } else {
                                // Handle invalid login response
                                Toast.makeText(MainActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            // Handle network errors
                            Toast.makeText(MainActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    private boolean checkPermissions() {
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return cameraPermission == PackageManager.PERMISSION_GRANTED &&
                locationPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_REQUEST_CODE
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Call to superclass

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean cameraGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean locationGranted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (!cameraGranted || !locationGranted) {
                    Toast.makeText(this, "Permissions are required to use the app!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Permission request was denied.", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void navigateToHomeScreen(String role, String username) {
        Intent intent;
        if (role.equals("Admin")) {
            intent = new Intent(MainActivity.this, HomeActivity.class);
        } else {
            intent = new Intent(MainActivity.this, UserHomeActivity.class);
            intent.putExtra("username", username);  // Pass the username to UserHomeActivity
        }
        startActivity(intent);
    }
}
