package com.example.integration.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.integration.HomeActivity;
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
                    // Create the login request model
                    LoginRequest loginRequest = new LoginRequest(username, password);

                    // Make the API call using Retrofit
                    Retrofit retrofit = RetrofitClient.getRetrofitInstance("");  // Initially passing an empty token

                    ApiService apiService = retrofit.create(ApiService.class);
                    Call<LoginResponse> call = apiService.login(loginRequest);

                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.isSuccessful()) {
                                // Get the token from the response (if the login was successful)
                                String token = response.body().getToken();

                                // Now make the next API call with the token
                                Retrofit retrofitWithToken = RetrofitClient.getRetrofitInstance(token);

                                // Proceed with the next API call using the token
                                // Example: calling another API method here if needed, or just navigate to home
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // Handle unsuccessful login (e.g., invalid credentials)
                                Toast.makeText(MainActivity.this, "Invalid credentials! Response Code: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            // Handle network errors
                            Toast.makeText(MainActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("RetrofitClient", "Request failed: " + t.getMessage());
                        }
                    });
                }
            }
        });
    }
}
