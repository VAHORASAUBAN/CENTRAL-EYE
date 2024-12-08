package com.example.integration.activities;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.integration.R;

import java.util.List;

public class RequestItemAdapter extends RecyclerView.Adapter<RequestItemAdapter.RequestItemViewHolder> {

    private List<RequestItemModel> itemList;

    // Constructor to initialize the data list
    public RequestItemAdapter(List<RequestItemModel> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RequestItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new RequestItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestItemViewHolder holder, int position) {
        // Bind the data to the views
        RequestItemModel item = itemList.get(position);
        holder.assetName.setText(item.getAssetName());
        holder.totalAssets.setText(String.valueOf(item.getTotalAssets()));
        holder.availableAssets.setText(String.valueOf(item.getAvailableAssets()));
        holder.returnDate.setText(item.getReturnDate());

        // Handle button clicks
        holder.acceptButton.setOnClickListener(v -> {
            // Handle accept button click (e.g., update database or notify user)
        });
        holder.rejectButton.setOnClickListener(v -> {
            // Handle reject button click (e.g., update database or notify user)
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // ViewHolder class to hold the views for each item
    public static class RequestItemViewHolder extends RecyclerView.ViewHolder {
        TextView assetName, totalAssets, availableAssets, returnDate;
        Button acceptButton, rejectButton;

        public RequestItemViewHolder(View itemView) {
            super(itemView);
            assetName = itemView.findViewById(R.id.assetNameValue);
            totalAssets = itemView.findViewById(R.id.ToataAssetrequest);
            availableAssets = itemView.findViewById(R.id.Availableasset);
            returnDate = itemView.findViewById(R.id.returndate);
            acceptButton = itemView.findViewById(R.id.acceptbtn);
            rejectButton = itemView.findViewById(R.id.rejectbtn);
        }
    }
}
