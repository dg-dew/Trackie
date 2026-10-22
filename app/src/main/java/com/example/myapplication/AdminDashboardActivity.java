package com.example.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import java.util.Calendar;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private LinearLayout categoriesContainer;
    private ImageButton btnSort, btnProfileTop, btnNotifications, navAdd, navProfile, navHome, navOrders;
    private com.google.android.material.floatingactionbutton.FloatingActionButton navCamera;
    private TextView tvSeeAll, tvUsername;
    private ViewPager2 bannerViewPager;

    private RecyclerView rvPendingPackages;
    private ParcelCardAdapter pendingAdapter;

    private final String[] allCategories = {"e-commerce", "Food items", "instants", "handle with care items"};

    private final int[] bannerImages = {
            R.drawable.banner_logo,
            R.drawable.banner_community,
            R.drawable.banner_quote,
            R.drawable.banner_utility
    };

    private final Handler sliderHandler = new Handler(Looper.getMainLooper());
    private ActivityResultLauncher<Intent> addParcelLauncher;
    private ActivityResultLauncher<Intent> cameraParcelLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Seed preloaded parcels on first launch
        ParcelRepository.getInstance().preloadIfNeeded();

        categoriesContainer = findViewById(R.id.categories_container);
        btnSort             = findViewById(R.id.btn_sort);
        tvSeeAll            = findViewById(R.id.tv_see_all);
        btnProfileTop       = findViewById(R.id.btn_profile_top);
        btnNotifications    = findViewById(R.id.btn_notifications);
        tvUsername          = findViewById(R.id.tv_username_dashboard);
        bannerViewPager     = findViewById(R.id.banner_viewpager);
        rvPendingPackages   = findViewById(R.id.rv_pending_packages);
        navCamera           = findViewById(R.id.nav_camera);
        navAdd              = findViewById(R.id.nav_add);
        navProfile          = findViewById(R.id.nav_profile);
        navHome             = findViewById(R.id.nav_home);
        navOrders           = findViewById(R.id.nav_orders);

        String username = getIntent().getStringExtra("USERNAME");
        if (username != null && !username.isEmpty()) {
            tvUsername.setText(username);
        }

        addParcelLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        refreshParcelLists();
                    }
                });

        cameraParcelLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        refreshParcelLists();
                    }
                });

        setupBannerSlider();
        populateCategories();
        setupParcelRecyclerViews();

        btnProfileTop.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("USERNAME", tvUsername.getText().toString());
            intent.putExtra("USER_TYPE", "Admin");
            startActivity(intent);
        });

        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra("USERNAME", tvUsername.getText().toString());
            intent.putExtra("USER_TYPE", "Admin");
            startActivity(intent);
        });

        btnNotifications.setOnClickListener(v ->
                startActivity(new Intent(this, NotificationsActivity.class)));

        navCamera.setOnClickListener(v ->
                cameraParcelLauncher.launch(
                        new Intent(this, CameraParcelActivity.class)));

        navAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddParcelActivity.class);
            addParcelLauncher.launch(intent);
        });

        tvSeeAll.setOnClickListener(v ->
                startActivity(new Intent(this, AllCategoriesActivity.class)));

        btnSort.setOnClickListener(this::showSortMenu);

        navHome.setOnClickListener(v ->
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show());

        navOrders.setOnClickListener(v ->
                startActivity(new Intent(this, RemindersActivity.class)));
    }

    // ─── Parcel Lists ───────────────────────────────────────────────────────

    private void setupParcelRecyclerViews() {
        rvPendingPackages.setLayoutManager(new LinearLayoutManager(this));
        pendingAdapter = new ParcelCardAdapter(ParcelRepository.getInstance().getAllParcels());
        rvPendingPackages.setAdapter(pendingAdapter);
    }

    private void refreshParcelLists() {
        pendingAdapter.updateList(ParcelRepository.getInstance().getAllParcels());
    }

    // ─── Parcel Card Adapter ────────────────────────────────────────────────

    private static class ParcelCardAdapter extends RecyclerView.Adapter<ParcelCardAdapter.VH> {
        private List<ParcelRepository.ParcelModel> list;

        ParcelCardAdapter(List<ParcelRepository.ParcelModel> list) {
            this.list = new java.util.ArrayList<>(list);
        }

        void updateList(List<ParcelRepository.ParcelModel> newList) {
            this.list = new java.util.ArrayList<>(newList);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
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

        @Override
        public int getItemCount() {
            return list.size();
        }

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

    // ─── Banner Slider ──────────────────────────────────────────────────────

    private void setupBannerSlider() {
        bannerViewPager.setAdapter(new BannerAdapter(bannerImages));
        bannerViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 2500);
            }
        });
        sliderHandler.postDelayed(sliderRunnable, 2500);
    }

    private final Runnable sliderRunnable = () -> {
        int next = (bannerViewPager.getCurrentItem() + 1) % bannerImages.length;
        bannerViewPager.setCurrentItem(next);
    };

    // ─── Categories ─────────────────────────────────────────────────────────

    private void populateCategories() {
        categoriesContainer.removeAllViews();
        for (String cat : allCategories) {
            addCategoryBox(cat);
        }
    }

    private void addCategoryBox(String catName) {
        TextView tv = new TextView(this);
        tv.setText(catName);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(14);
        tv.setGravity(android.view.Gravity.CENTER);
        tv.setTypeface(null, android.graphics.Typeface.BOLD);
        tv.setPadding(16, 0, 16, 0);

        float d = getResources().getDisplayMetrics().density;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (int)(130 * d), (int)(80 * d));
        params.setMargins(0, 0, (int)(12 * d), 0);
        tv.setLayoutParams(params);

        android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
        gd.setColor(Color.parseColor("#3E2723"));
        gd.setCornerRadius(20 * d);
        tv.setBackground(gd);

        tv.setOnClickListener(v -> {
            if ("e-commerce".equals(catName)) {
                startActivity(new Intent(this, EcommerceActivity.class));
            } else {
                Toast.makeText(this, "Clicked: " + catName, Toast.LENGTH_SHORT).show();
            }
        });
        categoriesContainer.addView(tv);
    }

    // ─── Sort ───────────────────────────────────────────────────────────────

    private void showSortMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenu().add("By Date");
        popup.getMenu().add("By Name");
        popup.getMenu().add("By Floor");
        popup.getMenu().add("By Building");
        popup.getMenu().add("By Room No");
        popup.setOnMenuItemClickListener(item -> {
            if ("By Date".equals(item.getTitle())) {
                showDatePicker();
            } else {
                Toast.makeText(this, "Sorting " + item.getTitle(), Toast.LENGTH_SHORT).show();
            }
            return true;
        });
        popup.show();
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        new DatePickerDialog(this,
                (view, year, month, day) ->
                        Toast.makeText(this,
                                "Selected: " + day + "/" + (month + 1) + "/" + year,
                                Toast.LENGTH_SHORT).show(),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    // ─── Lifecycle ──────────────────────────────────────────────────────────

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 2500);
        if (pendingAdapter != null) {
            refreshParcelLists();
        }
    }

    // ─── Banner Adapter ─────────────────────────────────────────────────────

    private class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BVH> {
        private final int[] images;

        BannerAdapter(int[] images) {
            this.images = images;
        }

        @NonNull
        @Override
        public BVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_banner, parent, false);
            return new BVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BVH holder, int position) {
            holder.imageView.setImageResource(images[position]);
        }

        @Override
        public int getItemCount() {
            return images.length;
        }

        class BVH extends RecyclerView.ViewHolder {
            ImageView imageView;

            BVH(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.iv_banner);
            }
        }
    }
}