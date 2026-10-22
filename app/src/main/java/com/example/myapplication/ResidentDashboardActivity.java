package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import java.util.List;

public class ResidentDashboardActivity extends AppCompatActivity {

    private TextView tvUsername;
    private ViewPager2 bannerViewPager;
    private RecyclerView rvMyParcels;
    private ImageButton btnProfileTop, btnNotifications, navHome, navReturn, navComplaint, navSettings;

    private final int[] bannerImages = {
            R.drawable.banner_logo,
            R.drawable.banner_community,
            R.drawable.banner_quote,
            R.drawable.banner_utility
    };

    private final Handler sliderHandler = new Handler(Looper.getMainLooper());
    private final Runnable sliderRunnable = () -> {
        int next = (bannerViewPager.getCurrentItem() + 1) % bannerImages.length;
        bannerViewPager.setCurrentItem(next);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_dashboard);

        ParcelRepository.getInstance().preloadIfNeeded();

        tvUsername       = findViewById(R.id.tv_username_dashboard);
        bannerViewPager  = findViewById(R.id.banner_viewpager);
        rvMyParcels      = findViewById(R.id.rv_my_parcels);
        btnProfileTop    = findViewById(R.id.btn_profile_top);
        btnNotifications = findViewById(R.id.btn_notifications);
        navHome          = findViewById(R.id.nav_home);
        navReturn        = findViewById(R.id.nav_return);
        navComplaint     = findViewById(R.id.nav_complaint);
        navSettings      = findViewById(R.id.nav_settings);

        String username = getIntent().getStringExtra("USERNAME");
        if (username != null && !username.isEmpty()) tvUsername.setText(username);

        setupBannerSlider();
        setupMyParcels(username);

        // Quick action buttons
        findViewById(R.id.action_my_parcels).setOnClickListener(v ->
                rvMyParcels.smoothScrollToPosition(0));

        findViewById(R.id.action_return).setOnClickListener(v ->
                startActivity(new Intent(this, ReturnItemsActivity.class)));

        findViewById(R.id.action_complaint).setOnClickListener(v ->
                startActivity(new Intent(this, FileComplaintActivity.class)));

        findViewById(R.id.action_track).setOnClickListener(v ->
                startActivity(new Intent(this, TrackOrderActivity.class)));

        // Top bar
        btnProfileTop.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("USERNAME", tvUsername.getText().toString());
            intent.putExtra("USER_TYPE", "Resident");
            startActivity(intent);
        });

        btnNotifications.setOnClickListener(v ->
                startActivity(new Intent(this, NotificationsActivity.class)));

        // Bottom nav
        navHome.setOnClickListener(v ->
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show());

        navReturn.setOnClickListener(v ->
                startActivity(new Intent(this, ReturnItemsActivity.class)));

        navComplaint.setOnClickListener(v ->
                startActivity(new Intent(this, FileComplaintActivity.class)));

        navSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra("USERNAME", tvUsername.getText().toString());
            intent.putExtra("USER_TYPE", "Resident");
            startActivity(intent);
        });

        // FAB camera → opens CameraParcelActivity
        findViewById(R.id.nav_camera).setOnClickListener(v ->
                startActivity(new Intent(this, CameraParcelActivity.class)));
    }

    private void setupMyParcels(String username) {
        rvMyParcels.setLayoutManager(new LinearLayoutManager(this));

        java.util.List<ParcelRepository.ParcelModel> mine = new java.util.ArrayList<>();

        if (username != null && !username.trim().isEmpty()) {
            // Strict: only parcels whose residentName exactly matches (case-insensitive)
            for (ParcelRepository.ParcelModel p : ParcelRepository.getInstance().getAllParcels()) {
                if (p.residentName.equalsIgnoreCase(username.trim())) {
                    mine.add(p);
                }
            }

            // Fallback: partial match if no exact match (handles "Rahul" matching "Rahul Sharma")
            if (mine.isEmpty()) {
                for (ParcelRepository.ParcelModel p : ParcelRepository.getInstance().getAllParcels()) {
                    String[] parts = username.trim().split("\\s+");
                    for (String part : parts) {
                        if (part.length() > 2 && p.residentName.toLowerCase()
                                .contains(part.toLowerCase())) {
                            mine.add(p);
                            break;
                        }
                    }
                }
            }
        }

        // Show empty state message if no parcels found
        TextView tvEmpty = findViewById(R.id.tv_no_parcels);
        if (mine.isEmpty()) {
            tvEmpty.setVisibility(android.view.View.VISIBLE);
            rvMyParcels.setVisibility(android.view.View.GONE);
        } else {
            tvEmpty.setVisibility(android.view.View.GONE);
            rvMyParcels.setVisibility(android.view.View.VISIBLE);
        }

        rvMyParcels.setAdapter(new ParcelMiniAdapter(mine));
    }

    // ── Simple Parcel Adapter ─────────────────────────────────────────────

    private static class ParcelMiniAdapter
            extends RecyclerView.Adapter<ParcelMiniAdapter.VH> {

        private final List<ParcelRepository.ParcelModel> list;

        ParcelMiniAdapter(List<ParcelRepository.ParcelModel> list) {
            this.list = list;
        }

        @NonNull @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_parcel_card, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            ParcelRepository.ParcelModel p = list.get(position);
            holder.tvName.setText(p.residentName);
            holder.tvDetails.setText("Room " + p.room + "  •  " + p.building);
            holder.tvBadge.setText(p.type);
            holder.tvBrand.setText(p.brand != null ? p.brand : p.type);
            if (holder.tvDate != null) holder.tvDate.setText("📅 " + p.dateAdded);
        }

        @Override public int getItemCount() { return list.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvName, tvDetails, tvBadge, tvBrand, tvDate;
            VH(@NonNull View itemView) {
                super(itemView);
                tvName    = itemView.findViewById(R.id.tv_resident_name);
                tvDetails = itemView.findViewById(R.id.tv_parcel_details);
                tvBadge   = itemView.findViewById(R.id.tv_parcel_type_badge);
                tvBrand   = itemView.findViewById(R.id.tv_brand_badge);
                tvDate    = itemView.findViewById(R.id.tv_date_added);
            }
        }
    }

    // ── Banner Slider ─────────────────────────────────────────────────────

    private void setupBannerSlider() {
        bannerViewPager.setAdapter(new BannerAdapter(bannerImages));
        bannerViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override public void onPageSelected(int position) {
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 2500);
            }
        });
        sliderHandler.postDelayed(sliderRunnable, 2500);
    }

    @Override protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 2500);
    }

    private class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BVH> {
        private final int[] images;
        BannerAdapter(int[] images) { this.images = images; }

        @NonNull @Override
        public BVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_banner, parent, false);
            return new BVH(v);
        }

        @Override public void onBindViewHolder(@NonNull BVH h, int pos) {
            h.iv.setImageResource(images[pos]);
        }

        @Override public int getItemCount() { return images.length; }

        class BVH extends RecyclerView.ViewHolder {
            ImageView iv;
            BVH(@NonNull View v) { super(v); iv = v.findViewById(R.id.iv_banner); }
        }
    }
}