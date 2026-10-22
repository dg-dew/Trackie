package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        String username = getIntent().getStringExtra("USERNAME");

        // ── Back ──────────────────────────────────────────────────────────────
        findViewById(R.id.btn_settings_back).setOnClickListener(v -> finish());

        // ── Shortcuts ─────────────────────────────────────────────────────────
        String userType = getIntent().getStringExtra("USER_TYPE");

        findViewById(R.id.tv_shortcut_account).setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("USERNAME", username);
            intent.putExtra("USER_TYPE", userType);
            startActivity(intent);
        });

        findViewById(R.id.tv_shortcut_password).setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPasswordActivity.class)));

        findViewById(R.id.tv_shortcut_notifications).setOnClickListener(v ->
                startActivity(new Intent(this, NotificationsActivity.class)));

        findViewById(R.id.tv_shortcut_delete).setOnClickListener(v ->
                showDeleteConfirmation());

        // ── Legal ─────────────────────────────────────────────────────────────
        findViewById(R.id.tv_legal_terms).setOnClickListener(v ->
                showInfoPopup("Terms & Conditions",
                        "Account Security\n" +
                                "You are responsible for keeping your login credentials safe and secure.\n\n" +
                                "Community Guidelines\n" +
                                "Our app is built to help you \"make friends for life.\" We expect all residents to interact respectfully within the app and the hostel.\n\n" +
                                "Service Usage\n" +
                                "The package tracking and utility features are provided to make your everyday life easier. Please do not misuse or spam these systems.\n\n" +
                                "Updates\n" +
                                "We reserve the right to update or modify these terms at any time to improve your experience."));

        findViewById(R.id.tv_legal_privacy).setOnClickListener(v ->
                showInfoPopup("Privacy Policy",
                        "What We Collect\n" +
                                "We collect basic information such as your name, email address, and room/floor number when you create an account.\n\n" +
                                "How We Use It\n" +
                                "Your data is used exclusively to provide app services—such as notifying you when a package arrives, managing your utility requests, and securing your account.\n\n" +
                                "Data Protection\n" +
                                "We implement strict security measures to keep your data safe. We do not sell, trade, or rent your personal information to outside companies."));

        findViewById(R.id.tv_legal_cancellation).setOnClickListener(v ->
                showInfoPopup("Cancellation Policy",
                        "Utility & Maintenance Requests\n" +
                                "If you submit a utility request by mistake, please cancel it within the app at least 2 hours before the scheduled service.\n\n" +
                                "Package Returns\n" +
                                "If you change your mind about returning an item, please update the status in the \"Return Items\" tab immediately so our admin team is aware.\n\n" +
                                "Refunds\n" +
                                "If any in-app payments are made and later canceled, approved refunds will be processed back to your original payment method within 5–7 business days."));
    }

    // ── Dialogs ───────────────────────────────────────────────────────────────

    private void showInfoPopup(String title, String message) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_info);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        ((TextView) dialog.findViewById(R.id.tv_info_title)).setText(title);
        ((TextView) dialog.findViewById(R.id.tv_info_message)).setText(message);
        dialog.findViewById(R.id.btn_info_close).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void showDeleteConfirmation() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_delete_account);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        dialog.findViewById(R.id.btn_delete_no).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.btn_delete_yes).setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(this, "Account Deleted", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        dialog.show();
    }
}