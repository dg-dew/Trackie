package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class TrackOrderActivity extends AppCompatActivity {

    private EditText etTrackingId;
    private Button btnSearch;
    private ImageButton btnBack;
    private View cardResult;
    private TextView tvTrackingName, tvTrackingIdResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);

        etTrackingId     = findViewById(R.id.et_tracking_id);
        btnSearch        = findViewById(R.id.btn_track_search);
        btnBack          = findViewById(R.id.btn_track_back);
        cardResult       = findViewById(R.id.card_tracking_result);
        tvTrackingName   = findViewById(R.id.tv_tracking_name);
        tvTrackingIdResult = findViewById(R.id.tv_tracking_id_result);

        btnBack.setOnClickListener(v -> finish());

        btnSearch.setOnClickListener(v -> {
            String query = etTrackingId.getText().toString().trim();
            if (query.isEmpty()) {
                Toast.makeText(this, "Please enter a name or tracking ID", Toast.LENGTH_SHORT).show();
                return;
            }
            searchParcel(query);
        });
    }

    private void searchParcel(String query) {
        List<ParcelRepository.ParcelModel> all = ParcelRepository.getInstance().getAllParcels();
        ParcelRepository.ParcelModel found = null;

        for (ParcelRepository.ParcelModel p : all) {
            if (p.residentName.toLowerCase().contains(query.toLowerCase())
                    || (p.brand != null && p.brand.toLowerCase().contains(query.toLowerCase()))) {
                found = p;
                break;
            }
        }

        if (found != null) {
            cardResult.setVisibility(View.VISIBLE);
            tvTrackingName.setText("Parcel for " + found.residentName);
            tvTrackingIdResult.setText("Room " + found.room + "  •  " + found.brand
                    + "  •  " + found.building);
        } else {
            cardResult.setVisibility(View.GONE);
            Toast.makeText(this, "No parcel found for \"" + query + "\"", Toast.LENGTH_LONG).show();
        }
    }
}