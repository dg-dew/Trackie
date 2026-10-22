package com.example.myapplication;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1. Message: Trackie Opened
        Toast.makeText(this, "Trackie Opened", Toast.LENGTH_SHORT).show();

        // 2. Find the views
        TextView loginTitle = findViewById(R.id.loginTitle);
        View buttonsContainer = findViewById(R.id.buttons_container);
        Button btnStudent = findViewById(R.id.btn_student);
        Button btnWarden = findViewById(R.id.btn_warden);

        // 3. Animate the views
        ObjectAnimator titleAnimator = ObjectAnimator.ofFloat(loginTitle, "translationY", -500f, 0f);
        titleAnimator.setDuration(2000);
        titleAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        titleAnimator.start();

        ObjectAnimator buttonAnimator = ObjectAnimator.ofFloat(buttonsContainer, "translationY", 500f, 0f);
        buttonAnimator.setDuration(2000);
        buttonAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        buttonAnimator.start();

        // 4. Logic for RESIDENT Button - Now points to StudentWardenLoginActivity
        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, StudentWardenLoginActivity.class);
                intent.putExtra("USER_TYPE", "Resident");
                startActivity(intent);
            }
        });

        // 5. Logic for ADMIN Button
        btnWarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, StudentWardenLoginActivity.class);
                intent.putExtra("USER_TYPE", "Admin");
                startActivity(intent);
            }
        });
    }
}