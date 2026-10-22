package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class CameraParcelActivity extends AppCompatActivity {

    private ImageView ivPhoto;
    private LinearLayout llPlaceholder, llDatePicker, btnRetake;
    private AutoCompleteTextView actvName, actvBuilding;
    private EditText etPhone, etRoom;
    private TextView tvDate;
    private RadioGroup rgType;
    private Button btnSave;

    private String selectedDate = "";
    private ActivityResultLauncher<Intent> cameraLauncher;

    private final String[] preloadedNames = {
            "Dew Dasgupta", "Dew Smith", "Dew Sharma", "Dew Patel", "Dew Nair",
            "John Smith", "John Doe", "John Wick", "John Abraham", "John Matthew",
            "Sara Johnson", "Sara Miller", "Sara White", "Sara Iyer", "Sara Khan",
            "Rahul Sharma", "Rahul Gupta", "Rahul Verma", "Rahul Mehta", "Rahul Singh",
            "Priya Nair", "Priya Menon", "Priya Sharma", "Priya Pillai", "Priya Das",
            "Arjun Kapoor", "Arjun Reddy", "Arjun Nair", "Arjun Mathew", "Arjun Raj",
            "Ananya Sen", "Ananya Krishnan", "Ananya Roy", "Ananya Bose", "Ananya Joshi",
            "Rohan Malhotra", "Rohan Verma", "Rohan Tiwari", "Rohan Kumar", "Rohan Shah"
    };

    private final String[] buildings = {
            "Serenity New Boys", "Serenity New Girls", "Serenity Empire",
            "Serenity Homes", "Serenity Vel", "Serenity Corner House",
            "Serenity Christ View", "Serenity LNG 3", "Serenity Spencer",
            "Serenity LNG 2", "Serenity Tulip", "Serenity Co-Living PG",
            "Serenity Girls Hostel", "Serenity Mounts View"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_parcel);

        ivPhoto       = findViewById(R.id.iv_captured_photo);
        llPlaceholder = findViewById(R.id.ll_photo_placeholder);
        llDatePicker  = findViewById(R.id.ll_cp_date);
        btnRetake     = findViewById(R.id.btn_retake);
        actvName      = findViewById(R.id.actv_cp_name);
        actvBuilding  = findViewById(R.id.actv_cp_building);
        etPhone       = findViewById(R.id.et_cp_phone);
        etRoom        = findViewById(R.id.et_cp_room);
        tvDate        = findViewById(R.id.tv_cp_date);
        rgType        = findViewById(R.id.rg_cp_type);
        btnSave       = findViewById(R.id.btn_cp_save);

        // Default date = today
        selectedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        tvDate.setText(selectedDate);

        // Camera launcher — returns thumbnail bitmap
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        if (extras != null) {
                            Bitmap photo = (Bitmap) extras.get("data");
                            if (photo != null) {
                                ivPhoto.setImageBitmap(photo);
                                llPlaceholder.setVisibility(View.GONE);
                                btnRetake.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });

        // Auto-launch camera straight away when this page opens
        launchCamera();

        // Back
        ((ImageButton) findViewById(R.id.btn_camera_parcel_back))
                .setOnClickListener(v -> finish());

        // Retake
        btnRetake.setVisibility(View.GONE);
        btnRetake.setOnClickListener(v -> launchCamera());

        // Name autocomplete → auto-fill phone
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, preloadedNames);
        actvName.setAdapter(nameAdapter);
        actvName.setThreshold(1);
        actvName.setOnItemClickListener((parent, view, position, id) -> {
            String phone = "9" + (100000000 + new Random().nextInt(900000000));
            etPhone.setText(phone);
        });

        // Building dropdown
        ArrayAdapter<String> buildingAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, buildings);
        actvBuilding.setAdapter(buildingAdapter);
        actvBuilding.setOnClickListener(v -> actvBuilding.showDropDown());

        // Date picker
        llDatePicker.setOnClickListener(v -> {
            final Calendar cal = Calendar.getInstance();
            try {
                Date d = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(selectedDate);
                if (d != null) cal.setTime(d);
            } catch (Exception ignored) {}

            new DatePickerDialog(this, (view, year, month, day) -> {
                Calendar picked = Calendar.getInstance();
                picked.set(year, month, day);
                selectedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        .format(picked.getTime());
                tvDate.setText(selectedDate);
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Save
        btnSave.setOnClickListener(v -> saveParcel());
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            cameraLauncher.launch(intent);
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveParcel() {
        String name     = actvName.getText().toString().trim();
        String phone    = etPhone.getText().toString().trim();
        String building = actvBuilding.getText().toString().trim();
        String room     = etRoom.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter resident name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (building.isEmpty()) {
            Toast.makeText(this, "Please select a building", Toast.LENGTH_SHORT).show();
            return;
        }
        if (room.isEmpty()) {
            Toast.makeText(this, "Please enter room number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (rgType.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a parcel type", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton rb = findViewById(rgType.getCheckedRadioButtonId());
        String type = rb.getText().toString();

        ParcelRepository.ParcelModel parcel =
                new ParcelRepository.ParcelModel(name, phone, building, room, type, type);
        parcel.dateAdded = selectedDate;

        ParcelRepository.getInstance().addParcel(parcel);

        Toast.makeText(this, "✅  Parcel saved to Pending Packages!", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
    }
}