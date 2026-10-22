package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;
import java.util.List;

public class EcommerceActivity extends AppCompatActivity {

    private RecyclerView rvBrands;
    private List<String> brands = Arrays.asList("Nykaa", "Amazon", "Flipkart", "Tira", "Purple", "Ajio", "Myntra");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecommerce);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        rvBrands = findViewById(R.id.rv_ecommerce_brands);
        rvBrands.setLayoutManager(new LinearLayoutManager(this));
        rvBrands.setAdapter(new BrandAdapter(brands));
    }

    private class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {
        private List<String> list;

        public BrandAdapter(List<String> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_brand_box, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String brandName = list.get(position);
            
            if ("Nykaa".equals(brandName)) {
                // For Nykaa, show the logo and hide text
                holder.tvName.setVisibility(View.GONE);
                holder.tvShopNow.setVisibility(View.GONE);
                holder.ivLogo.setVisibility(View.VISIBLE);
                
                // --- INSTRUCTIONS ---
                // 1. Add your Nykaa image to the 'res/drawable' folder
                // 2. Name it 'nykaa_logo'
                // 3. Uncomment the line below:
                // holder.ivLogo.setImageResource(R.drawable.nykaa_logo);
                
                // Using launcher icon as a temporary placeholder so you can see it working
                holder.ivLogo.setImageResource(R.mipmap.ic_launcher); 
                
            } else {
                // For other brands, show the name and "Shop now"
                holder.tvName.setVisibility(View.VISIBLE);
                holder.tvShopNow.setVisibility(View.VISIBLE);
                holder.ivLogo.setVisibility(View.GONE);
                holder.tvName.setText(brandName);
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvShopNow;
            ImageView ivLogo;
            
            ViewHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tv_brand_name);
                tvShopNow = itemView.findViewById(R.id.tv_shop_now);
                ivLogo = itemView.findViewById(R.id.iv_brand_logo);
            }
        }
    }
}
