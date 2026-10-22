package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etConfirmPassword;
    private Button btnSignup;
    private ImageButton btnPhone, btnGoogle, btnApple;
    private TextView tvLoginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_signup);

        etEmail = findViewById(R.id.et_signup_email);
        etPassword = findViewById(R.id.et_signup_password);
        etConfirmPassword = findViewById(R.id.et_signup_confirm_password);
        btnSignup = findViewById(R.id.btn_signup);
        tvLoginLink = findViewById(R.id.tv_login_link);

        btnPhone = findViewById(R.id.btn_signup_phone);
        btnGoogle = findViewById(R.id.btn_signup_google);
        btnApple = findViewById(R.id.btn_signup_apple);

        // 2. Setup Click Listeners
        setupListeners();
    }

    private void setupListeners() {
        // --- Sign Up Button ---
        btnSignup.setOnClickListener(v -> {
            if (validateInputs()) {
                showSuccessDialog();
            }
        });

        // --- Social Login Buttons (Same as Login Page) ---
        btnPhone.setOnClickListener(v -> showPhoneLoginDialog());

        btnGoogle.setOnClickListener(v -> {
            Uri webpage = Uri.parse("https://accounts.google.com");
            startActivity(new Intent(Intent.ACTION_VIEW, webpage));
        });

        btnApple.setOnClickListener(v -> {
            Uri appleUrl = Uri.parse("https://appleid.apple.com/sign-in");
            startActivity(new Intent(Intent.ACTION_VIEW, appleUrl));
        });

        // --- Back to Login Link ---
        tvLoginLink.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private boolean validateInputs() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void showPhoneLoginDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_phone_login);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        Button btnSendOtp = dialog.findViewById(R.id.btn_send_otp);
        Button btnVerifyOtp = dialog.findViewById(R.id.btn_verify_otp);

        btnSendOtp.setOnClickListener(v -> Toast.makeText(this, "OTP Sent", Toast.LENGTH_SHORT).show());
        btnVerifyOtp.setOnClickListener(v -> {
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showSuccessDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_registration_success);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // ── Animate the tick circle + draw the tick path ──────────────────
        android.view.View tickCircle = dialog.findViewById(R.id.v_tick_circle);
        android.widget.ImageView ivTick = dialog.findViewById(R.id.iv_tick);

        // 1. Scale the whole circle in with overshoot
        android.view.animation.Animation scaleIn =
                android.view.animation.AnimationUtils.loadAnimation(this, R.anim.scale_in);
        tickCircle.startAnimation(scaleIn);
        ivTick.startAnimation(scaleIn);

        // 2. After circle appears, draw the tick path
        ivTick.postDelayed(() -> {
            android.graphics.drawable.Animatable animatable =
                    (android.graphics.drawable.Animatable) ivTick.getDrawable();
            if (animatable != null) animatable.start();
        }, 200);

        Button btnDone = dialog.findViewById(R.id.btn_success_done);
        btnDone.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }
}