package com.example.integration.activities.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.integration.R;
import com.example.integration.activities.admin.Request_details;
import com.example.integration.activities.model.RequestItemModel;

import androidx.fragment.app.FragmentActivity;

import java.util.List;

public class RequestItemAdapter extends RecyclerView.Adapter<RequestItemAdapter.RequestItemViewHolder> {

    private List<RequestItemModel> itemList;
    private Context context;

    public RequestItemAdapter(Context context, List<RequestItemModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RequestItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new RequestItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestItemViewHolder holder, int position) {
        RequestItemModel item = itemList.get(position);
        holder.assetName.setText(item.getAssetName());
        holder.totalAssets.setText(String.valueOf(item.getTotalAssets()));
        holder.availableAssets.setText(String.valueOf(item.getAvailableAssets()));
        holder.returnDate.setText(item.getReturnDate());

        // Handle item click to redirect to Request_details
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("requestId", item.getAssetName());
            bundle.putInt("totalAssets", item.getTotalAssets());
            bundle.putInt("availableAssets", item.getAvailableAssets());
            bundle.putString("returnDate", item.getReturnDate());

            Request_details detailsFragment = new Request_details();
            detailsFragment.setArguments(bundle);

            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailsFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class RequestItemViewHolder extends RecyclerView.ViewHolder {
        TextView assetName, totalAssets, availableAssets, returnDate;

        public RequestItemViewHolder(View itemView) {
            super(itemView);
            assetName = itemView.findViewById(R.id.assetNameValue);
            totalAssets = itemView.findViewById(R.id.ToataAssetrequest);
            availableAssets = itemView.findViewById(R.id.Availableasset);
            returnDate = itemView.findViewById(R.id.returndate);
        }
    }
}
