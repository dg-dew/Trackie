package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView rvNotifications;

    // Sample notifications data
    private List<NotificationItem> notifications = Arrays.asList(
            new NotificationItem("Student Message", "I have received my parcel from Amazon. Thanks!", "2 mins ago"),
            new NotificationItem("New Parcel", "A new parcel arrived for Room 302", "1 hour ago"),
            new NotificationItem("Warden Update", "Building B maintenance scheduled for tomorrow", "3 hours ago"),
            new NotificationItem("Student Message", "Can you check if my Flipkart order arrived?", "5 hours ago")
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        findViewById(R.id.btn_notifications_back).setOnClickListener(v -> finish());

        rvNotifications = findViewById(R.id.rv_notifications);
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        rvNotifications.setAdapter(new NotificationAdapter(notifications));
    }

    private static class NotificationItem {
        String title, message, time;
        NotificationItem(String title, String message, String time) {
            this.title = title;
            this.message = message;
            this.time = time;
        }
    }

    private class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
        private List<NotificationItem> list;

        public NotificationAdapter(List<NotificationItem> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            NotificationItem item = list.get(position);
            holder.tvTitle.setText(item.title);
            holder.tvMessage.setText(item.message);
            holder.tvTime.setText(item.time);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvMessage, tvTime;
            ViewHolder(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_notification_title);
                tvMessage = itemView.findViewById(R.id.tv_notification_message);
                tvTime = itemView.findViewById(R.id.tv_notification_time);
            }
        }
    }
}
