package com.example.integration.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(intent);
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

//    private void navigateToHome(String token) {
//        // Save or use the token for authenticated API calls
//        Retrofit retrofitWithToken = RetrofitClient.getRetrofitInstance(token);
//        // Proceed to the HomeActivity
//        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//        startActivity(intent);
//        finish();
//    }
}
