package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class StudentWardenLoginActivity extends AppCompatActivity {

    // UI Elements
    private EditText etUsername, etPassword;
    private AutoCompleteTextView actvBuilding;
    private CheckBox cbRememberMe;
    private TextView tvForgotPassword, tvSignup, tvTitle;
    private Button btnLogin;
    private ImageButton btnPhone, btnGoogle, btnApple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_warden_login);

        // 1. Initialize all views
        initViews();

        // 2. Setup Building Dropdown List
        setupBuildingDropdown();

        // 3. Setup Click Listeners
        setupListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        actvBuilding = findViewById(R.id.actv_building);
        etPassword = findViewById(R.id.et_password);

        cbRememberMe = findViewById(R.id.cb_remember_me);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        btnLogin = findViewById(R.id.btn_login);

        btnPhone = findViewById(R.id.btn_phone);
        btnGoogle = findViewById(R.id.btn_google);
        btnApple = findViewById(R.id.btn_apple);

        tvSignup = findViewById(R.id.tv_signup);
        tvTitle = findViewById(R.id.tv_title);

        // --- Set the Title (Resident or Admin) ---
        String userType = getIntent().getStringExtra("USER_TYPE");
        if (userType != null) {
            tvTitle.setText(userType + " Login");
        }

        // Hide Warden/Security radio group for Resident login
        RadioGroup rgRole = findViewById(R.id.rg_role);
        if ("Resident".equals(userType)) {
            rgRole.setVisibility(android.view.View.GONE);
        }
    }

    private void setupBuildingDropdown() {
        List<String> buildings = new ArrayList<>();
        buildings.add("Serenity New Boys");
        buildings.add("Serenity New Girls");
        buildings.add("Serenity Empire");
        buildings.add("Serenity Homes");
        buildings.add("Serenity Vel");
        buildings.add("Serenity Corner House");
        buildings.add("Serenity Christ View");
        buildings.add("Serenity LNG 3");
        buildings.add("Serenity Spencer");
        buildings.add("Serenity LNG 2");
        buildings.add("Serenity Tulip");
        buildings.add("Serenity Co-Living PG");
        buildings.add("Serenity Girls Hostel");
        buildings.add("Serenity Mounts View");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                buildings
        );

        actvBuilding.setAdapter(adapter);

        // Force Show Dropdown on Click
        actvBuilding.setOnClickListener(v -> actvBuilding.showDropDown());
        actvBuilding.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) actvBuilding.showDropDown();
        });
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> {
            String userType = getIntent().getStringExtra("USER_TYPE");
            String username = etUsername.getText().toString().trim();
            String building = actvBuilding.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validation: ensure all fields are filled
            if (username.isEmpty() || building.isEmpty() || password.isEmpty() || building.equals("Select Building")) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if ("Admin".equals(userType)) {
                Intent intent = new Intent(StudentWardenLoginActivity.this, AdminDashboardActivity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            } else {
                Intent resIntent = new Intent(StudentWardenLoginActivity.this, ResidentDashboardActivity.class);
                resIntent.putExtra("USERNAME", username);
                startActivity(resIntent);
            }
        });

        btnGoogle.setOnClickListener(v -> {
            Uri webpage = Uri.parse("https://accounts.google.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(intent);
        });

        btnApple.setOnClickListener(v -> {
            Uri appleUrl = Uri.parse("https://appleid.apple.com/sign-in");
            Intent intent = new Intent(Intent.ACTION_VIEW, appleUrl);
            startActivity(intent);
        });

        btnPhone.setOnClickListener(v -> showPhoneLoginDialog());

        tvSignup.setOnClickListener(v -> {
            startActivity(new Intent(StudentWardenLoginActivity.this, SignupActivity.class));
        });

        tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(StudentWardenLoginActivity.this, ForgotPasswordActivity.class));
        });
    }

    private void showPhoneLoginDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_phone_login);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        EditText etPhoneNumber = dialog.findViewById(R.id.et_phone_number);
        EditText etOtp = dialog.findViewById(R.id.et_otp);
        Button btnSendOtp = dialog.findViewById(R.id.btn_send_otp);
        Button btnVerifyOtp = dialog.findViewById(R.id.btn_verify_otp);

        btnSendOtp.setOnClickListener(v -> {
            String ph = etPhoneNumber.getText().toString().trim();
            if (ph.isEmpty() || ph.length() < 10) {
                Toast.makeText(this, "Please enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "OTP Sent to " + ph, Toast.LENGTH_SHORT).show();
            }
        });

        btnVerifyOtp.setOnClickListener(v -> {
            String otp = etOtp.getText().toString().trim();
            if (otp.isEmpty()) {
                Toast.makeText(this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            String userType = getIntent().getStringExtra("USER_TYPE");
            String username = etUsername.getText().toString(); // Use the one from the main screen if available
            if (username.isEmpty()) username = "Phone User";

            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            if ("Admin".equals(userType)) {
                Intent intent = new Intent(this, AdminDashboardActivity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            } else {
                Intent resIntent = new Intent(this, ResidentDashboardActivity.class);
                resIntent.putExtra("USERNAME", username);
                startActivity(resIntent);
            }
        });

        dialog.show();
    }
}