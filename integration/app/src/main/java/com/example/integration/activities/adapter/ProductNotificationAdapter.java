package com.example.integration.activities.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.integration.R;
import com.example.integration.activities.model.Notification;

import java.util.List;

// Adapter Class for RecyclerView
public class ProductNotificationAdapter extends RecyclerView.Adapter<ProductNotificationAdapter.NotificationViewHolder> {

    // List to hold notifications
    private final List<Notification> notifications;

    // Listener for item click events
    private final OnNotificationClickListener clickListener;

    // Constructor
    public ProductNotificationAdapter(List<Notification> notifications, OnNotificationClickListener clickListener) {
        this.notifications = notifications;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single notification item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_message, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        // Get the notification for the current position
        Notification notification = notifications.get(position);

        // Bind the data to the view
        holder.titleTextView.setText(notification.getTitle());
        holder.messageTextView.setText(notification.getMessage());

        // Set click listener for the item
//        holder.cardView.setOnClickListener(v -> {
//            if (clickListener != null) {
//                clickListener.onNotificationClick(notification);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        // Return the size of the notification list
        return notifications == null ? 0 : notifications.size();
    }

    // ViewHolder Class for Notification Items
    static class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView; // Title TextView
        TextView messageTextView; // Message TextView
//        CardView cardView; // Parent CardView

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views
            titleTextView = itemView.findViewById(R.id.notification_title);
            messageTextView = itemView.findViewById(R.id.notification_message);
//            cardView = itemView.findViewById(R.id.card_view); // Ensure your XML file has this ID
        }
    }

    // Interface for handling click events
    public interface OnNotificationClickListener {
        void onNotificationClick(Notification notification);
    }
}
