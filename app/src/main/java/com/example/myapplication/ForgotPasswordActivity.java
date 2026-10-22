package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ForgotPasswordActivity extends AppCompatActivity {

    private CardView cardFindAccount, cardResetPassword;
    private EditText etFindInput, etOtp, etNewPassword, etConfirmPassword;
    private Button btnSendOtp, btnResetFinal;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // 1. Initialize Views
        cardFindAccount = findViewById(R.id.card_find_account);
        cardResetPassword = findViewById(R.id.card_reset_password);
        
        etFindInput = findViewById(R.id.et_find_input);
        etOtp = findViewById(R.id.et_forgot_otp);
        etNewPassword = findViewById(R.id.et_new_password_forgot);
        etConfirmPassword = findViewById(R.id.et_confirm_password_forgot);
        
        btnSendOtp = findViewById(R.id.btn_send_otp_forgot);
        btnResetFinal = findViewById(R.id.btn_reset_password_final);
        btnBack = findViewById(R.id.btn_forgot_back);

        // 2. Setup Listeners
        setupListeners();
    }

    private void setupListeners() {
        // Back Button
        btnBack.setOnClickListener(v -> finish());

        // Step 1: Send OTP Button
        btnSendOtp.setOnClickListener(v -> {
            String input = etFindInput.getText().toString().trim();
            if (input.isEmpty()) {
                Toast.makeText(this, "Please enter your details", Toast.LENGTH_SHORT).show();
            } else {
                // Transition to Step 2
                Toast.makeText(this, "OTP sent to " + input, Toast.LENGTH_SHORT).show();
                cardFindAccount.setVisibility(View.GONE);
                cardResetPassword.setVisibility(View.VISIBLE);
            }
        });

        // Step 2: Final Reset Button
        btnResetFinal.setOnClickListener(v -> {
            if (validateResetInputs()) {
                Toast.makeText(this, "Password successfully reset!", Toast.LENGTH_LONG).show();
                finish(); // Go back to login
            }
        });
    }

    private boolean validateResetInputs() {
        String otp = etOtp.getText().toString().trim();
        String newPass = etNewPassword.getText().toString().trim();
        String confirmPass = etConfirmPassword.getText().toString().trim();

        if (otp.length() < 6) {
            Toast.makeText(this, "Please enter the 6-digit OTP", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (newPass.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
