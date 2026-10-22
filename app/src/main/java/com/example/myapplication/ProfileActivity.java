package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private ActivityResultLauncher<String> getContent;
    private TextView tvProfileName;
    private EditText etProfileName, etProfileEmail, etProfilePhone, etProfileRoom;
    private AutoCompleteTextView actvBuilding;
    private TextView tvEditBuilding, tvEditRoom;

    // Preloaded resident data (in a real app these come from a DB/preferences)
    private static final String PRELOADED_BUILDING = "Serenity New Boys";
    private static final String PRELOADED_ROOM     = "204";
    private static final String PRELOADED_PHONE    = "9876543210";
    private static final String PRELOADED_EMAIL    = "resident@serenity.in";

    private final String[] buildings = {
            "Serenity New Boys", "Serenity New Girls", "Serenity Empire",
            "Serenity Homes", "Serenity Vel", "Serenity Corner House",
            "Serenity Christ View", "Serenity LNG 3", "Serenity Spencer",
            "Serenity LNG 2", "Serenity Tulip", "Serenity Co-Living PG",
            "Serenity Girls Hostel", "Serenity Mounts View"
    };

    private boolean buildingEditMode = false;
    private boolean roomEditMode     = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvProfileName  = findViewById(R.id.tv_profile_name_top);
        etProfileName  = findViewById(R.id.et_profile_name);
        etProfileEmail = findViewById(R.id.et_profile_email);
        etProfilePhone = findViewById(R.id.et_profile_phone);
        etProfileRoom  = findViewById(R.id.et_profile_room);
        actvBuilding   = findViewById(R.id.actv_profile_building);
        tvEditBuilding = findViewById(R.id.tv_edit_building);
        tvEditRoom     = findViewById(R.id.tv_edit_room);

        // Populate from intent / preloaded defaults
        String username = getIntent().getStringExtra("USERNAME");
        String userType = getIntent().getStringExtra("USER_TYPE");
        if (username != null && !username.isEmpty()) {
            tvProfileName.setText(username);
            etProfileName.setText(username);
        }
        etProfileEmail.setText(PRELOADED_EMAIL);
        etProfilePhone.setText(PRELOADED_PHONE);
        etProfileRoom.setText(PRELOADED_ROOM);
        actvBuilding.setText(PRELOADED_BUILDING);

        // Hide Building and Room fields for Admin — only Residents have these
        boolean isAdmin = "Admin".equals(userType);
        int vis = isAdmin ? android.view.View.GONE : android.view.View.VISIBLE;
        findViewById(R.id.ll_building_row).setVisibility(vis);
        actvBuilding.setVisibility(vis);
        findViewById(R.id.ll_room_row).setVisibility(vis);
        etProfileRoom.setVisibility(vis);

        // Building dropdown adapter (used when edit mode is on)
        ArrayAdapter<String> buildingAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, buildings);
        actvBuilding.setAdapter(buildingAdapter);

        // ── Edit Building toggle ─────────────────────────────────────────
        tvEditBuilding.setOnClickListener(v -> {
            buildingEditMode = !buildingEditMode;
            actvBuilding.setEnabled(buildingEditMode);
            actvBuilding.setFocusable(buildingEditMode);
            actvBuilding.setFocusableInTouchMode(buildingEditMode);
            actvBuilding.setClickable(buildingEditMode);
            if (buildingEditMode) {
                tvEditBuilding.setText("✔ Done");
                actvBuilding.requestFocus();
                actvBuilding.showDropDown();
            } else {
                tvEditBuilding.setText("✏ Edit");
                Toast.makeText(this, "Building updated", Toast.LENGTH_SHORT).show();
            }
        });

        // ── Edit Room toggle ─────────────────────────────────────────────
        tvEditRoom.setOnClickListener(v -> {
            roomEditMode = !roomEditMode;
            etProfileRoom.setEnabled(roomEditMode);
            etProfileRoom.setFocusable(roomEditMode);
            etProfileRoom.setFocusableInTouchMode(roomEditMode);
            if (roomEditMode) {
                tvEditRoom.setText("✔ Done");
                etProfileRoom.requestFocus();
            } else {
                tvEditRoom.setText("✏ Edit");
                Toast.makeText(this, "Room number updated", Toast.LENGTH_SHORT).show();
            }
        });

        // Back
        findViewById(R.id.btn_profile_back).setOnClickListener(v -> finish());

        // Photo picker
        getContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null)
                        Toast.makeText(this, "Photo selected", Toast.LENGTH_SHORT).show();
                });
        ((FrameLayout) findViewById(R.id.btn_change_photo))
                .setOnClickListener(v -> getContent.launch("image/*"));

        // Logout
        findViewById(R.id.tv_logout).setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Delete account
        findViewById(R.id.tv_delete_account).setOnClickListener(v -> showDeleteConfirmation());
    }

    private void showDeleteConfirmation() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_delete_account);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        dialog.findViewById(R.id.btn_delete_no).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.btn_delete_yes).setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(this, "Account Deleted", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        dialog.show();
    }
}