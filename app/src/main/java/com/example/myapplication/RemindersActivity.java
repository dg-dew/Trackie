package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RemindersActivity extends AppCompatActivity {

    private RecyclerView rvReminders;
    private TextView tvCount, tvEmptyMessage, tabReminders, tabArrived;
    private LinearLayout llEmpty;
    private ParcelListAdapter adapter;

    private boolean showingReminders = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        rvReminders  = findViewById(R.id.rv_reminders);
        tvCount      = findViewById(R.id.tv_reminder_count);
        llEmpty      = findViewById(R.id.ll_empty);
        tvEmptyMessage = findViewById(R.id.tv_empty_message);
        tabReminders = findViewById(R.id.tab_reminders);
        tabArrived   = findViewById(R.id.tab_arrived);

        findViewById(R.id.btn_reminders_back).setOnClickListener(v -> finish());

        rvReminders.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ParcelListAdapter(ParcelRepository.getInstance().getReminders());
        rvReminders.setAdapter(adapter);

        updateTabUI(true);
        updateList(true);

        tabReminders.setOnClickListener(v -> {
            showingReminders = true;
            updateTabUI(true);
            updateList(true);
        });

        tabArrived.setOnClickListener(v -> {
            showingReminders = false;
            updateTabUI(false);
            updateList(false);
        });
    }

    private void updateTabUI(boolean remindersActive) {
        if (remindersActive) {
            tabReminders.setBackgroundResource(R.drawable.button_background_circle);
            tabReminders.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(0xFF3E2723));
            tabReminders.setTextColor(0xFFE9E0C3);
            tabArrived.setBackground(null);
            tabArrived.setTextColor(0xFF6D4C41);
        } else {
            tabArrived.setBackgroundResource(R.drawable.button_background_circle);
            tabArrived.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(0xFF3E2723));
            tabArrived.setTextColor(0xFFE9E0C3);
            tabReminders.setBackground(null);
            tabReminders.setTextColor(0xFF6D4C41);
        }
    }

    private void updateList(boolean reminders) {
        List<ParcelRepository.ParcelModel> list = reminders
                ? ParcelRepository.getInstance().getReminders()
                : ParcelRepository.getInstance().getArrivedToday();

        tvCount.setText(String.valueOf(list.size()));

        if (list.isEmpty()) {
            rvReminders.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
            tvEmptyMessage.setText(reminders
                    ? "No overdue parcels right now! 🎉"
                    : "No parcels arrived today yet.");
        } else {
            rvReminders.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            adapter.updateList(list);
        }
    }

    // ── Adapter ──────────────────────────────────────────────────────────────

    static class ParcelListAdapter extends RecyclerView.Adapter<ParcelListAdapter.VH> {
        private List<ParcelRepository.ParcelModel> list;

        ParcelListAdapter(List<ParcelRepository.ParcelModel> list) {
            this.list = new java.util.ArrayList<>(list);
        }

        void updateList(List<ParcelRepository.ParcelModel> newList) {
            this.list = new java.util.ArrayList<>(newList);
            notifyDataSetChanged();
        }

        @NonNull @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_parcel_card, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH h, int position) {
            ParcelRepository.ParcelModel p = list.get(position);
            h.tvName.setText(p.residentName);
            h.tvDetails.setText("Room " + p.room + "  •  " + p.building);
            h.tvBadge.setText(p.type);
            h.tvBrand.setText(p.brand != null ? p.brand : p.type);
            if (h.tvDate != null) h.tvDate.setText("📅 " + p.dateAdded);
        }

        @Override public int getItemCount() { return list.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvName, tvDetails, tvBadge, tvBrand, tvDate;
            VH(@NonNull View v) {
                super(v);
                tvName    = v.findViewById(R.id.tv_resident_name);
                tvDetails = v.findViewById(R.id.tv_parcel_details);
                tvBadge   = v.findViewById(R.id.tv_parcel_type_badge);
                tvBrand   = v.findViewById(R.id.tv_brand_badge);
                tvDate    = v.findViewById(R.id.tv_date_added);
            }
        }
    }
}