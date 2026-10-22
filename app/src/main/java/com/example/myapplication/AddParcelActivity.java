package com.example.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class AddParcelActivity extends AppCompatActivity {

    private AutoCompleteTextView actvResidentName, actvBuilding;
    private EditText etPhone, etRoom;
    private TextView tvArrivalDate;
    private LinearLayout llDatePicker;
    private RadioGroup rgParcelType;
    private Button btnAdd;
    private ImageButton btnBack;

    // Stores the user-selected arrival date (dd/MM/yyyy)
    private String selectedDate = "";

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
        setContentView(R.layout.activity_add_parcel);

        actvResidentName = findViewById(R.id.actv_resident_name);
        actvBuilding     = findViewById(R.id.actv_add_building);
        etPhone          = findViewById(R.id.et_add_phone);
        etRoom           = findViewById(R.id.et_add_room);
        tvArrivalDate    = findViewById(R.id.tv_arrival_date);
        llDatePicker     = findViewById(R.id.ll_date_picker);
        rgParcelType     = findViewById(R.id.rg_parcel_type);
        btnAdd           = findViewById(R.id.btn_add_parcel_final);
        btnBack          = findViewById(R.id.btn_add_parcel_back);

        // Default date = today
        selectedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        tvArrivalDate.setText(selectedDate);

        setupAdapters();
        setupListeners();
    }

    private void setupAdapters() {
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, preloadedNames);
        actvResidentName.setAdapter(nameAdapter);
        actvResidentName.setThreshold(1);

        ArrayAdapter<String> buildingAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, buildings);
        actvBuilding.setAdapter(buildingAdapter);
        actvBuilding.setOnClickListener(v -> actvBuilding.showDropDown());
        actvBuilding.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) actvBuilding.showDropDown();
        });
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Auto-fill phone when name selected
        actvResidentName.setOnItemClickListener((parent, view, position, id) -> {
            String randomPhone = "9" + (100000000 + new Random().nextInt(900000000));
            etPhone.setText(randomPhone);
        });

        // Date picker
        llDatePicker.setOnClickListener(v -> showDatePicker());

        // Add button
        btnAdd.setOnClickListener(v -> {
            if (validateFields()) {
                int selectedId = rgParcelType.getCheckedRadioButtonId();
                RadioButton rb = findViewById(selectedId);
                String type = rb.getText().toString();

                ParcelRepository.ParcelModel parcel = new ParcelRepository.ParcelModel(
                        actvResidentName.getText().toString().trim(),
                        etPhone.getText().toString().trim(),
                        actvBuilding.getText().toString().trim(),
                        etRoom.getText().toString().trim(),
                        type,
                        type
                );
                // Override the auto-set date with the user's chosen arrival date
                parcel.dateAdded = selectedDate;

                ParcelRepository.getInstance().addParcel(parcel);
                Toast.makeText(this, "Parcel Added Successfully!", Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void showDatePicker() {
        final Calendar cal = Calendar.getInstance();
        // Parse current selected date into calendar
        try {
            Date d = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(selectedDate);
            if (d != null) cal.setTime(d);
        } catch (Exception ignored) {}

        new DatePickerDialog(this, (view, year, month, day) -> {
            Calendar picked = Calendar.getInstance();
            picked.set(year, month, day);
            selectedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(picked.getTime());
            tvArrivalDate.setText(selectedDate);
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private boolean validateFields() {
        if (actvResidentName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter resident name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (actvBuilding.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please select a building", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etRoom.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter room number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Please select date of arrival", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (rgParcelType.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a parcel type", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}