package com.example.patt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    // Declare buttons
    private Button privacyPolicyBtn, termsConditionsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info); // Link to activity_info.xml

        // Initialize buttons
        privacyPolicyBtn = findViewById(R.id.privacyPolicyBtn);
        termsConditionsBtn = findViewById(R.id.termsConditionsBtn);

        // Handle Privacy Policy button click
        privacyPolicyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Privacy Policy Activity
                openPrivacyPolicy();
            }
        });

        // Handle Terms & Conditions button click
        termsConditionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Terms & Conditions Activity
                openTermsConditions();
            }
        });
    }

    // Method to open Privacy Policy Activity
    private void openPrivacyPolicy() {
        Intent intent = new Intent(InfoActivity.this, PrivacyPolicyActivity.class);
        startActivity(intent);
    }

    // Method to open Terms & Conditions Activity
    private void openTermsConditions() {
        Intent intent = new Intent(InfoActivity.this, TermsAndConditionsActivity.class);
        startActivity(intent);
    }
}
