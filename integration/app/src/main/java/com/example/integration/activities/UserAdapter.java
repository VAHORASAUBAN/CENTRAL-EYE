package com.example.integration.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.integration.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        // Log data for debugging
        System.out.println("Binding User: " + user.getFull_name());
        System.out.println("Role: " + user.getRole());
        System.out.println("Station: " + user.getStation());

        // Safely bind data to the views
        holder.userName.setText(user.getFull_name() != null ? user.getFull_name() : "N/A");
        holder.userTitle.setText(user.getRole() != null ? user.getRole() : "N/A");
        holder.userLocation.setText(user.getStation() != null ? user.getStation() : "N/A");

        // Set default image
        holder.userImage.setImageResource(R.drawable.profile);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userName, userTitle, userLocation;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            userName = itemView.findViewById(R.id.userName);
            userTitle = itemView.findViewById(R.id.userTitle);
            userLocation = itemView.findViewById(R.id.userLocation);
        }
    }
}
