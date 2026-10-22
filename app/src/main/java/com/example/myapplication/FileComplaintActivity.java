package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class FileComplaintActivity extends AppCompatActivity {

    private AutoCompleteTextView actvCategory;
    private EditText etSubject, etDescription;
    private RadioGroup rgPriority;
    private Button btnSubmit;
    private ImageButton btnBack;

    private final String[] categories = {
            "Parcel Not Received",
            "Damaged Parcel",
            "Wrong Parcel Delivered",
            "Delayed Delivery",
            "Maintenance Issue",
            "Noise Complaint",
            "Cleanliness Issue",
            "Staff Behaviour",
            "Internet / Utilities",
            "Other"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_complaint);

        actvCategory   = findViewById(R.id.actv_complaint_category);
        etSubject      = findViewById(R.id.et_complaint_subject);
        etDescription  = findViewById(R.id.et_complaint_description);
        rgPriority     = findViewById(R.id.rg_priority);
        btnSubmit      = findViewById(R.id.btn_submit_complaint);
        btnBack        = findViewById(R.id.btn_complaint_back);

        // Category dropdown
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, categories);
        actvCategory.setAdapter(adapter);
        actvCategory.setOnClickListener(v -> actvCategory.showDropDown());
        actvCategory.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) actvCategory.showDropDown();
        });

        btnBack.setOnClickListener(v -> finish());

        btnSubmit.setOnClickListener(v -> {
            if (validate()) {
                Toast.makeText(this,
                        "Complaint submitted! We'll get back to you soon.",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private boolean validate() {
        if (actvCategory.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etSubject.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter a subject", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etDescription.getText().toString().trim().length() < 10) {
            Toast.makeText(this, "Please describe your complaint in detail", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}