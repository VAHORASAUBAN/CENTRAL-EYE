package com.example.integration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.journeyapps.barcodescanner.CaptureActivity;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button scanButton = findViewById(R.id.scan_barcode_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start BarcodeScannerActivity for scanning
                Intent intent = new Intent(HomeActivity.this, BarcodeScannerActivity.class);
                startActivityForResult(intent, 0); // Use a requestCode here
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the scan was successful
        if (resultCode == RESULT_OK && data != null) {
            // Extract the scan result from the Intent
            String scannedResult = data.getStringExtra("SCAN_RESULT");

            if (scannedResult != null && !scannedResult.isEmpty()) {
                // Display the scanned result using a Toast message
                Toast.makeText(this, "Scanned: " + scannedResult, Toast.LENGTH_LONG).show();
            } else {
                // If no result is found, show an error
                Toast.makeText(this, "Scan result is empty", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle scan cancellation or failure
            Toast.makeText(this, "Scan canceled or failed", Toast.LENGTH_SHORT).show();
        }
    }
}
